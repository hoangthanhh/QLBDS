package com.qlbds.controller.admin;

import com.qlbds.dto.property.PropertyDetailDTO;
import com.qlbds.dto.property.PropertySaveDTO;
import com.qlbds.dto.property.PropertySummaryDTO;
import com.qlbds.service.PropertyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminPropertyController", value = {"/admin/bds", "/admin/property"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class AdminPropertyController extends HttpServlet {

    private final PropertyService propertyService = new PropertyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // 1. AJAX LẤY CHI TIẾT BĐS VÀ TOÀN BỘ DANH SÁCH ẢNH ĐỂ ĐỔ LÊN MODAL
        if ("get-detail".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                PropertyDetailDTO detail = propertyService.getPropertyDetail(id);

                if (detail != null) {
                    resp.setContentType("application/json;charset=UTF-8");

                    StringBuilder imgJson = new StringBuilder("[");
                    if (detail.getImageUrls() != null) {
                        for (int i = 0; i < detail.getImageUrls().size(); i++) {
                            imgJson.append("\"").append(escapeJson(detail.getImageUrls().get(i))).append("\"");
                            if (i < detail.getImageUrls().size() - 1) imgJson.append(",");
                        }
                    }
                    imgJson.append("]");

                    String json = "{"
                            + "\"id\":" + detail.getId() + ","
                            + "\"title\":\"" + escapeJson(detail.getTitle()) + "\","
                            + "\"address\":\"" + escapeJson(detail.getAddress()) + "\","
                            + "\"price\":" + (detail.getPrice() != null ? detail.getPrice() : 0) + ","
                            + "\"area\":" + (detail.getArea() != null ? detail.getArea() : 0) + ","
                            + "\"propertyType\":\"" + (detail.getPropertyType() != null ? detail.getPropertyType() : "APARTMENT") + "\","
                            + "\"description\":\"" + escapeJson(detail.getDescription()) + "\","
                            + "\"images\":" + imgJson
                            + "}";
                    resp.getWriter().write(json);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            return;
        }

        // 2. HIỂN THỊ DANH SÁCH & PHÂN TRANG / LỌC
        int page = 1;
        try {
            page = Math.max(1, Integer.parseInt(req.getParameter("page")));
        } catch (Exception ignored) {
        }

        int pageSize = 5;
        String keyword = req.getParameter("keyword");
        String priceRange = req.getParameter("priceRange");
        String propertyType = req.getParameter("propertyType");

        // --- ĐÃ THÊM: Hứng tham số status từ JSP ---
        String statusFilter = req.getParameter("status");
        if (statusFilter == null || statusFilter.trim().isEmpty()) {
            statusFilter = "ALL";
        }

        // --- ĐÃ SỬA: Truyền thêm statusFilter vào Service ---
        List<PropertySummaryDTO> propertyList = propertyService.getPropertiesByPage(page, pageSize, keyword, priceRange, propertyType, statusFilter);
        int totalPages = propertyService.getTotalPages(pageSize, keyword, priceRange, propertyType, statusFilter);

        req.setAttribute("productList", propertyList);
        req.setAttribute("currentPage", page);
        req.setAttribute("pageSize", pageSize);
        req.setAttribute("totalPage", totalPages > 0 ? totalPages : 1);
        req.setAttribute("keyword", keyword);
        req.setAttribute("priceRange", priceRange);
        req.setAttribute("propertyType", propertyType);

        // --- ĐÃ THÊM: Trả lại currentStatus để JSP giữ được thẻ option đang chọn ---
        req.setAttribute("currentStatus", statusFilter);

        req.getRequestDispatcher("/WEB-INF/views/admin/adminBDS.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        // 1. AJAX THÊM MỚI / SỬA BĐS
        if ("create".equals(action) || "update".equals(action)) {
            PropertySaveDTO dto = new PropertySaveDTO();
            if ("update".equals(action)) {
                try {
                    dto.setId(Integer.parseInt(req.getParameter("id")));
                } catch (Exception ignored) {
                }
            }
            dto.setTitle(req.getParameter("title"));
            dto.setAddress(req.getParameter("address"));
            dto.setPropertyType(req.getParameter("propertyType"));

            try {
                dto.setPrice(Double.parseDouble(req.getParameter("price")));
            } catch (Exception ignored) {
            }
            try {
                dto.setArea(Double.parseDouble(req.getParameter("area")));
            } catch (Exception ignored) {
            }
            dto.setDescription(req.getParameter("description"));

            // Đọc tập tin nhiều ảnh upload (Lọc chỉ lấy các file có dữ liệu)
            List<Part> imageParts = new ArrayList<>();
            try {
                for (Part part : req.getParts()) {
                    if ("images".equals(part.getName()) && part.getSize() > 0 && part.getSubmittedFileName() != null && !part.getSubmittedFileName().trim().isEmpty()) {
                        imageParts.add(part);
                    }
                }
            } catch (Exception ignored) {
            }
            dto.setImageParts(imageParts);

            // Fix đường dẫn tuyệt đối thư mục uploads/properties
            String rootPath = req.getServletContext().getRealPath("");
            String uploadPath = rootPath + (rootPath.endsWith(File.separator) ? "" : File.separator) + "uploads" + File.separator + "properties";

            List<String> errors = "create".equals(action)
                    ? propertyService.createProperty(dto, uploadPath)
                    : propertyService.updateProperty(dto, uploadPath);

            resp.setContentType("application/json;charset=UTF-8");
            StringBuilder jsonErrors = new StringBuilder("[");
            for (int i = 0; i < errors.size(); i++) {
                jsonErrors.append("\"").append(escapeJson(errors.get(i))).append("\"");
                if (i < errors.size() - 1) jsonErrors.append(",");
            }
            jsonErrors.append("]");

            resp.getWriter().write("{\"success\":" + errors.isEmpty() + ",\"errors\":" + jsonErrors + "}");
            return;
        }

        // 2. FORM XÓA BĐS TRÊN BẢNG
        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                String res = propertyService.deleteProperty(id);

                session.setAttribute("msg", "SUCCESS".equals(res) ? "Xóa Bất Động Sản thành công!" : res);
                session.setAttribute("msgType", "SUCCESS".equals(res) ? "success" : "danger");
            } catch (Exception e) {
                session.setAttribute("msg", "Dữ liệu yêu cầu không hợp lệ!");
                session.setAttribute("msgType", "danger");
            }
        }

        // 3. FORM BỂ KÈO (MỞ BÁN LẠI)
        if ("reopen".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                String res = propertyService.reopenProperty(id);
                session.setAttribute("msg", "SUCCESS".equals(res) ? "Đã hủy cọc và đưa BĐS về trạng thái Mở bán!" : res);
                session.setAttribute("msgType", "SUCCESS".equals(res) ? "success" : "danger");
            } catch (Exception e) {
                session.setAttribute("msg", "Dữ liệu yêu cầu không hợp lệ!");
                session.setAttribute("msgType", "danger");
            }
            resp.sendRedirect(req.getContextPath() + "/admin/bds");
            return;
        }

        // 4. AJAX XÓA TỪNG ẢNH
        if ("delete-image".equals(action)) {
            try {
                int imageId = Integer.parseInt(req.getParameter("imageId"));
                boolean success = propertyService.deletePropertyImage(imageId);
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().write("{\"success\":" + success + "}");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            return;
        }

        // 5. KHÔI PHỤC BĐS ĐÃ XÓA MỀM
        if ("restore".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                String res = propertyService.restoreProperty(id);
                session.setAttribute("msg", "SUCCESS".equals(res) ? "Đã khôi phục và mở bán lại Bất động sản!" : res);
                session.setAttribute("msgType", "SUCCESS".equals(res) ? "success" : "danger");
            } catch (Exception e) {
                session.setAttribute("msg", "Dữ liệu yêu cầu không hợp lệ!");
                session.setAttribute("msgType", "danger");
            }
            resp.sendRedirect(req.getContextPath() + "/admin/bds");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin/bds");
    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "\\n");
    }
}