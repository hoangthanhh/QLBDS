package com.qlbds.controller.admin;

import com.qlbds.dto.property.PropertyCreateUpdateDTO;
import com.qlbds.dto.property.PropertyDetailDTO;
import com.qlbds.dto.property.PropertySummaryDTO;
import com.qlbds.service.PropertyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminPropertyController", value = {"/admin/bds", "/admin/property"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class AdminPropertyController extends HttpServlet {

    private PropertyService propertyService = new PropertyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // 1. AJAX LẤY CHI TIẾT BĐS ĐỂ ĐỔ LÊN MODAL SỬA (CHỐNG VỠ CHUỖI JSON)
        if ("get-detail".equals(action)) {
            try {
                Integer id = Integer.parseInt(req.getParameter("id"));
                PropertyDetailDTO detail = propertyService.getPropertyDetail(id);

                resp.setContentType("application/json;charset=UTF-8");

                if (detail != null) {
                    String title = detail.getTitle() != null ? detail.getTitle().replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "\\n") : "";
                    String address = detail.getAddress() != null ? detail.getAddress().replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "\\n") : "";
                    String description = detail.getDescription() != null ? detail.getDescription().replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "\\n") : "";

                    StringBuilder json = new StringBuilder("{");
                    json.append("\"id\":").append(detail.getId()).append(",");
                    json.append("\"title\":\"").append(title).append("\",");
                    json.append("\"address\":\"").append(address).append("\",");
                    json.append("\"price\":").append(detail.getPrice() != null ? detail.getPrice() : 0).append(",");
                    json.append("\"area\":").append(detail.getArea() != null ? detail.getArea() : 0).append(",");
                    json.append("\"propertyType\":\"").append(detail.getPropertyType() != null ? detail.getPropertyType() : "APARTMENT").append("\",");
                    json.append("\"description\":\"").append(description).append("\"");
                    json.append("}");

                    resp.getWriter().write(json.toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        // 2. HIỂN THỊ DANH SÁCH & PHÂN TRANG / LỌC NÂNG CAO
        int page = 1;
        int pageSize = 5;

        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException ignored) {
            }
        }

        String keyword = req.getParameter("keyword");
        String priceRange = req.getParameter("priceRange");
        String propertyType = req.getParameter("propertyType");

        List<PropertySummaryDTO> propertyList = propertyService.getPropertiesByPage(page, pageSize, keyword, priceRange, propertyType);
        int totalPages = propertyService.getTotalPages(pageSize, keyword, priceRange, propertyType);

        req.setAttribute("productList", propertyList);
        req.setAttribute("currentPage", page);
        req.setAttribute("pageSize", pageSize);
        req.setAttribute("totalPage", totalPages > 0 ? totalPages : 1);
        req.setAttribute("keyword", keyword);
        req.setAttribute("priceRange", priceRange);
        req.setAttribute("propertyType", propertyType);

        req.getRequestDispatcher("/WEB-INF/views/admin/adminBDS.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        // XỬ LÝ LƯU (THÊM / SỬA BẰNG AJAX)
        if ("create".equals(action) || "update".equals(action)) {
            PropertyCreateUpdateDTO dto = new PropertyCreateUpdateDTO();
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

            // Đọc tập tin nhiều ảnh upload (nếu có)
            List<Part> imageParts = new ArrayList<>();
            try {
                for (Part part : req.getParts()) {
                    if ("images".equals(part.getName()) && part.getSize() > 0) {
                        imageParts.add(part);
                    }
                }
            } catch (Exception ignored) {
            }
            dto.setImageParts(imageParts);

            String uploadPath = req.getServletContext().getRealPath("") + "uploads" + java.io.File.separator + "properties";
            List<String> errors = "create".equals(action)
                    ? propertyService.createProperty(dto, uploadPath)
                    : propertyService.updateProperty(dto, uploadPath);

            boolean isSuccess = errors.isEmpty();
            resp.setContentType("application/json;charset=UTF-8");
            StringBuilder jsonErrors = new StringBuilder("[");
            for (int i = 0; i < errors.size(); i++) {
                jsonErrors.append("\"").append(errors.get(i).replace("\"", "\\\"")).append("\"");
                if (i < errors.size() - 1) jsonErrors.append(",");
            }
            jsonErrors.append("]");

            resp.getWriter().write("{\"success\":" + isSuccess + ", \"errors\":" + jsonErrors.toString() + "}");
            return;
        }

        // XỬ LÝ XÓA
        if ("delete".equals(action)) {
            try {
                Integer id = Integer.parseInt(req.getParameter("id"));
                String res = propertyService.deleteProperty(id);

                session.setAttribute("msg", "SUCCESS".equals(res) ? "Xóa Bất Động Sản thành công!" : res);
                session.setAttribute("msgType", "SUCCESS".equals(res) ? "success" : "danger");
            } catch (Exception e) {
                session.setAttribute("msg", "Dữ liệu yêu cầu không hợp lệ!");
                session.setAttribute("msgType", "danger");
            }
        }

        resp.sendRedirect(req.getContextPath() + "/admin/bds");
    }
}