package com.qlbds.service;

import com.qlbds.constant.PropertyStatusEnum;
import com.qlbds.constant.PropertyTypeEnum;
import com.qlbds.dto.property.PropertyDetailDTO;
import com.qlbds.dto.property.PropertySaveDTO;
import com.qlbds.dto.property.PropertySummaryDTO;
import com.qlbds.entity.Property;
import com.qlbds.entity.PropertyImage;
import com.qlbds.repository.PropertyRepository;
import com.qlbds.repository.TransactionRepository;
import com.qlbds.util.ValidationUtil;

import javax.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PropertyService {

    private final PropertyRepository propertyRepository = new PropertyRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();

    // Customer

    // Lấy danh sách BĐS khả dụng hiển thị ngoài trang chủ và trang tìm kiếm của Khách hàng
    public List<PropertySummaryDTO> getPropertiesByPage(int page, int pageSize, String address, String priceRange, String propertyType) {
        List<Property> entities = propertyRepository.findAllAvailableByPage(page, pageSize, address, priceRange, propertyType);
        List<PropertySummaryDTO> dtos = new ArrayList<>();

        if (entities != null) {
            for (Property p : entities) {
                PropertySummaryDTO dto = new PropertySummaryDTO();
                dto.setId(p.getId());
                dto.setTitle(p.getTitle());
                dto.setAddress(p.getAddress());
                dto.setPrice(p.getPrice());
                dto.setArea(p.getArea());
                dto.setPropertyType(p.getPropertyType() != null ? p.getPropertyType().name() : "");
                dto.setStatus(p.getStatus() != null ? p.getStatus().name() : "");

                // Lấy ảnh đầu tiên làm ảnh chính đại diện
                if (p.getImages() != null && !p.getImages().isEmpty()) {
                    dto.setThumbnail(p.getImages().get(0).getImagePath());
                } else {
                    dto.setThumbnail("assets/customer/img/property-1.jpg");
                }
                dtos.add(dto);
            }
        }
        return dtos;
    }

    // Lấy chi tiết BĐS kèm toàn bộ danh sách ảnh cho trang xem chi tiết & modal sửa
    public PropertyDetailDTO getPropertyDetail(Integer id) {
        Property p = propertyRepository.findById(id);
        if (p == null) return null;

        PropertyDetailDTO dto = new PropertyDetailDTO();
        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setAddress(p.getAddress());
        dto.setPrice(p.getPrice());
        dto.setArea(p.getArea());
        dto.setPropertyType(p.getPropertyType() != null ? p.getPropertyType().name() : "");
        dto.setStatus(p.getStatus() != null ? p.getStatus().name() : "");
        dto.setDescription(p.getDescription());

        // Lấy tất cả ảnh cho trang / modal chi tiết
        List<String> imageUrls = new ArrayList<>();
        List<PropertyDetailDTO.ImageItem> imageItems = new ArrayList<>();

        if (p.getImages() != null) {
            for (PropertyImage img : p.getImages()) {
                imageUrls.add(img.getImagePath());
                imageItems.add(new PropertyDetailDTO.ImageItem(img.getId(), img.getImagePath()));
            }
        }
        dto.setImageUrls(imageUrls);
        dto.setImageItems(imageItems);

        return dto;
    }

    // Tính tổng số trang cho trang tìm kiếm của Customer
    public int getTotalPages(int pageSize, String address, String priceRange, String propertyType) {
        if (pageSize <= 0) pageSize = 6;
        long totalRecords = propertyRepository.countAvailableProperties(address, priceRange, propertyType);
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    // Staff & Admin

    // Thêm mới BĐS kèm danh sách ảnh tải lên (tối đa 5 ảnh)
    public List<String> createProperty(PropertySaveDTO dto, String uploadRealPath) {
        List<String> errors = ValidationUtil.validateProperty(dto, true);
        if (!errors.isEmpty()) return errors;

        try {
            Property p = new Property();
            p.setTitle(dto.getTitle().trim());
            p.setAddress(dto.getAddress().trim());
            p.setPrice(dto.getPrice() != null ? dto.getPrice().longValue() : 0L);
            p.setArea(dto.getArea() != null ? BigDecimal.valueOf(dto.getArea()) : BigDecimal.ZERO);
            p.setDescription(dto.getDescription());
            p.setPropertyType(PropertyTypeEnum.valueOf(dto.getPropertyType()));
            p.setStatus(PropertyStatusEnum.AVAILABLE);
            p.setIsDeleted(false);

            List<PropertyImage> images = saveUploadedFiles(dto.getImageParts(), uploadRealPath);

            if (!propertyRepository.saveProperty(p, images)) {
                errors.add("Lỗi hệ thống khi lưu BĐS vào cơ sở dữ liệu!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add("Lỗi xử lý dữ liệu: " + e.getMessage());
        }
        return errors;
    }

    // Cập nhật BĐS (Chặn nếu đã Đặt cọc/Đã bán hoặc đang có giao dịch PENDING chờ duyệt)
    public List<String> updateProperty(PropertySaveDTO dto, String uploadRealPath) {
        List<String> errors = ValidationUtil.validateProperty(dto, false);

        if (dto.getId() == null || dto.getId() <= 0) {
            errors.add("Mã BĐS không hợp lệ!");
            return errors;
        }

        if (!errors.isEmpty()) return errors;

        Property p = propertyRepository.findById(dto.getId());
        if (p == null) {
            errors.add("Bất động sản #" + dto.getId() + " không tồn tại!");
            return errors;
        }

        // Chặn sửa nếu BĐS đã Đặt cọc hoặc Đã bán đứt
        if (p.getStatus() == PropertyStatusEnum.DEPOSITED || p.getStatus() == PropertyStatusEnum.SOLD) {
            errors.add("Không thể sửa BĐS đã Đặt cọc hoặc Đã bán đứt!");
            return errors;
        }

        // Chặn sửa nếu đang có giao dịch PENDING chờ duyệt
        if (transactionRepository.hasAnyPendingTransaction(dto.getId())) {
            errors.add("Không thể sửa BĐS đang có yêu cầu giao dịch chờ duyệt. Vui lòng xử lý giao dịch trước!");
            return errors;
        }

        p.setTitle(dto.getTitle().trim());
        p.setAddress(dto.getAddress().trim());
        p.setPrice(dto.getPrice() != null ? dto.getPrice().longValue() : 0L);
        p.setArea(dto.getArea() != null ? BigDecimal.valueOf(dto.getArea()) : BigDecimal.ZERO);
        p.setDescription(dto.getDescription());
        p.setPropertyType(PropertyTypeEnum.valueOf(dto.getPropertyType()));

        List<PropertyImage> newImages = null;
        if (dto.getImageParts() != null && !dto.getImageParts().isEmpty()) {
            newImages = saveUploadedFiles(dto.getImageParts(), uploadRealPath);
        }

        if (!propertyRepository.updateProperty(p, newImages)) {
            errors.add("Lỗi hệ thống khi cập nhật cơ sở dữ liệu!");
        }
        return errors;
    }

    // Xóa mềm BĐS (Chặn nếu đã Đặt cọc/Đã bán, đang có giao dịch PENDING hoặc đã có lịch sử giao dịch)
    // ĐÃ SỬA: Xóa mềm BĐS (Đã bỏ chặn đối với BĐS bị hủy kèo)
    public String deleteProperty(Integer id) {
        Property p = propertyRepository.findById(id);
        if (p == null) return "Bất động sản không tồn tại!";

        if (p.getStatus() == PropertyStatusEnum.DEPOSITED || p.getStatus() == PropertyStatusEnum.SOLD) {
            return "Không thể xóa BĐS đã Đặt cọc hoặc Đã bán đứt!";
        }

        if (transactionRepository.hasAnyPendingTransaction(id)) {
            return "Không thể xóa! BĐS này đang có yêu cầu giao dịch chờ duyệt.";
        }

        // LƯU Ý: Đã xóa lệnh propertyRepository.hasTransactions(id) ở đây để cho phép xóa BĐS bị hủy

        return propertyRepository.deleteProperty(id) ? "SUCCESS" : "Lỗi hệ thống khi xóa BĐS!";
    }

    // Lấy danh sách BĐS cho trang Quản lý (hỗ trợ lọc từ khóa, khoảng giá, loại hình, trạng thái/xóa mềm)
    public List<PropertySummaryDTO> getPropertiesByPage(int page, int pageSize, String keyword, String priceRange, String propertyType, String statusFilter) {
        List<Property> entities = propertyRepository.findPropertiesByPage(page, pageSize, keyword, priceRange, propertyType, statusFilter);
        List<PropertySummaryDTO> dtos = new ArrayList<>();

        if (entities != null) {
            for (Property p : entities) {
                PropertySummaryDTO dto = new PropertySummaryDTO();
                dto.setId(p.getId());
                dto.setTitle(p.getTitle());
                dto.setAddress(p.getAddress());
                dto.setPrice(p.getPrice());
                dto.setArea(p.getArea());
                dto.setPropertyType(p.getPropertyType() != null ? p.getPropertyType().name() : "");
                // ĐÃ SỬA: Gọi trực tiếp từ Enum thay vì dùng chuỗi tĩnh "DELETED"
                if (p.getIsDeleted() != null && p.getIsDeleted()) {
                    dto.setStatus(PropertyStatusEnum.DELETED.name());
                } else {
                    dto.setStatus(p.getStatus() != null ? p.getStatus().name() : "");
                }

                if (p.getImages() != null && !p.getImages().isEmpty()) {
                    dto.setThumbnail(p.getImages().get(0).getImagePath());
                } else {
                    dto.setThumbnail("assets/customer/img/property-1.jpg");
                }
                dtos.add(dto);
            }
        }
        return dtos;
    }

    // Tính tổng số trang cho trang Quản lý theo bộ lọc
    public int getTotalPages(int pageSize, String keyword, String priceRange, String propertyType, String statusFilter) {
        if (pageSize <= 0) pageSize = 5;
        long totalRecords = propertyRepository.countTotalProperties(keyword, priceRange, propertyType, statusFilter);
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    // Xóa một ảnh cụ thể của BĐS theo ID ảnh
    public boolean deletePropertyImage(Integer imageId) {
        return propertyRepository.deleteImageById(imageId);
    }

    // ĐÃ SỬA: Nghiệp vụ Bể kèo (Khôi phục trạng thái BĐS + Hủy giao dịch cọc cũ)
    public String reopenProperty(Integer id) {
        Property p = propertyRepository.findById(id);
        if (p == null) return "Bất động sản không tồn tại!";

        if (p.getStatus() != PropertyStatusEnum.DEPOSITED) {
            return "Chỉ có thể Mở bán lại đối với BĐS đang ở trạng thái 'Đã nhận cọc' (DEPOSITED)!";
        }

        p.setStatus(PropertyStatusEnum.AVAILABLE);
        p.setIsDeleted(false);

        if (propertyRepository.update(p)) {
            // Tự động đánh dấu Hủy (CANCELLED) cho giao dịch cọc cũ
            transactionRepository.cancelCompletedDepositByProperty(id);
            return "SUCCESS";
        }
        return "Lỗi hệ thống khi mở bán lại!";
    }

    // Lưu tối đa 5 file ảnh tải lên vào thư mục máy chủ
    private List<PropertyImage> saveUploadedFiles(List<Part> parts, String uploadRealPath) {
        List<PropertyImage> images = new ArrayList<>();
        if (parts == null || parts.isEmpty()) return images;

        File uploadDir = new File(uploadRealPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        int count = 0;
        for (Part part : parts) {
            if (count >= 5) break; // Khóa chặt không vượt quá 5 ảnh

            if (part != null && part.getSize() > 0 && part.getSubmittedFileName() != null && !part.getSubmittedFileName().trim().isEmpty()) {
                String originalFilename = part.getSubmittedFileName();
                String fileExt = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
                String newFileName = UUID.randomUUID().toString() + fileExt;

                File fileToSave = new File(uploadDir, newFileName);
                try (InputStream input = part.getInputStream()) {
                    Files.copy(input, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    PropertyImage img = new PropertyImage();
                    img.setImagePath("uploads/properties/" + newFileName);
                    images.add(img);
                    count++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return images;
    }

    // Nghiệp vụ khôi phục BĐS bị xóa mềm
    public String restoreProperty(Integer id) {
        return propertyRepository.restoreProperty(id) ? "SUCCESS" : "Lỗi hệ thống khi khôi phục Bất động sản!";
    }
}