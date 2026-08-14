package com.qlbds.service;

import com.qlbds.constant.PropertyStatusEnum;
import com.qlbds.constant.PropertyTypeEnum;
import com.qlbds.dto.property.PropertyCreateUpdateDTO;
import com.qlbds.dto.property.PropertyDetailDTO;
import com.qlbds.dto.property.PropertySummaryDTO;
import com.qlbds.entity.Property;
import com.qlbds.entity.PropertyImage;
import com.qlbds.repository.PropertyRepository;
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

    private PropertyRepository propertyRepository = new PropertyRepository();


    // CUSTOMER (Trang chủ, Grid, Chi tiết BĐS)
    // DÙNG CHO TRANG CHỦ / GRID (Trả về Summary DTO)
    public List<PropertySummaryDTO> getPropertiesByPage(int page, int pageSize, String address, String priceRange, String propertyType) {
        List<Property> entities = propertyRepository.findAllAvailableByPage(page, pageSize, address, priceRange, propertyType);
        List<PropertySummaryDTO> dtos = new ArrayList<>();

        for (Property p : entities) {
            PropertySummaryDTO dto = new PropertySummaryDTO();
            dto.setId(p.getId());
            dto.setTitle(p.getTitle());
            dto.setAddress(p.getAddress());
            dto.setPrice(p.getPrice());
            dto.setArea(p.getArea());
            dto.setPropertyType(p.getPropertyType() != null ? p.getPropertyType().name() : "");
            dto.setStatus(p.getStatus() != null ? p.getStatus().name() : "");

            if (p.getImages() != null && !p.getImages().isEmpty()) {
                dto.setThumbnail(p.getImages().get(0).getImagePath());
            } else {
                dto.setThumbnail("assets/customer/img/property-1.jpg");
            }
            dtos.add(dto);
        }
        return dtos;
    }

    // DÙNG CHO TRANG CHI TIẾT (Trả về Detail DTO)
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

        List<String> imageUrls = new ArrayList<>();
        if (p.getImages() != null) {
            for (PropertyImage img : p.getImages()) {
                imageUrls.add(img.getImagePath());
            }
        }
        dto.setImageUrls(imageUrls);

        return dto;
    }

    public int getTotalPages(int pageSize, String address, String priceRange, String propertyType) {
        long totalRecords = propertyRepository.countAvailableProperties(address, priceRange, propertyType);
        return (int) Math.ceil((double) totalRecords / pageSize);
    }


    // ADMIN (Thêm mới, Cập nhật, Xóa ràng buộc, Upload ảnh)
    // 1. NGHIỆP VỤ THÊM MỚI BĐS KÈM UPLOAD NHIỀU ẢNH
    public List<String> createProperty(PropertyCreateUpdateDTO dto, String uploadRealPath) {
        List<String> errors = ValidationUtil.validateProperty(dto, true);
        if (!errors.isEmpty()) return errors;

        try {
            Property p = new Property();
            p.setTitle(dto.getTitle().trim());
            p.setAddress(dto.getAddress().trim());

            // Ép kiểu chuẩn theo Entity Property (price = Long, area = BigDecimal)
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

    // 2. NGHIỆP VỤ CẬP NHẬT BĐS
    public List<String> updateProperty(PropertyCreateUpdateDTO dto, String uploadRealPath) {
        List<String> errors = ValidationUtil.validateProperty(dto, false);

        // BẮT BỘC: Bắt lỗi nếu thiếu ID
        if (dto.getId() == null || dto.getId() <= 0) {
            errors.add("Mã BĐS không hợp lệ!");
            return errors;
        }

        if (!errors.isEmpty()) return errors;

        // LẤY CHÍNH XÁC ENTITY TỪ CSD T THEO ID NÀY
        Property p = propertyRepository.findById(dto.getId());
        if (p == null) {
            errors.add("Bất động sản #" + dto.getId() + " không tồn tại!");
            return errors;
        }

        // CHỈ CẬP NHẬT DỮ LIỆU CHO ĐÚNG BẢN GHI NÀY
        p.setTitle(dto.getTitle().trim());
        p.setAddress(dto.getAddress().trim());
        p.setPrice(dto.getPrice() != null ? dto.getPrice().longValue() : 0L);
        p.setArea(dto.getArea() != null ? java.math.BigDecimal.valueOf(dto.getArea()) : java.math.BigDecimal.ZERO);
        p.setDescription(dto.getDescription());
        p.setPropertyType(com.qlbds.constant.PropertyTypeEnum.valueOf(dto.getPropertyType()));

        // XỬ LÝ ẢNH MỚI UPLOAD (NẾU CÓ CHỌN FILE MỚI)
        List<PropertyImage> newImages = null;
        if (dto.getImageParts() != null && !dto.getImageParts().isEmpty()) {
            newImages = saveUploadedFiles(dto.getImageParts(), uploadRealPath);
        }

        // GỌI REPOSITORY CẬP NHẬT ĐÚNG ID
        if (!propertyRepository.updateProperty(p, newImages)) {
            errors.add("Lỗi hệ thống khi cập nhật cơ sở dữ liệu!");
        }
        return errors;
    }
    // 3. NGHIỆP VỤ XÓA BĐS (BẮT RÀNG BUỘC GIAO DỊCH)
    public String deleteProperty(Integer id) {
        Property p = propertyRepository.findById(id);
        if (p == null) return "Bất động sản không tồn tại!";

        // Kiểm tra ràng buộc dữ liệu: Đã có giao dịch thì CẤM xóa
        if (propertyRepository.hasTransactions(id)) {
            return "Không thể xóa! Bất động sản này đã phát sinh giao dịch đặt cọc hoặc mua hàng.";
        }

        return propertyRepository.deleteProperty(id) ? "SUCCESS" : "Lỗi hệ thống khi xóa BĐS!";
    }

    // HELPER: Ghi trực tiếp các tập tin ảnh được upload vào thư mục lưu trữ thực tế
    private List<PropertyImage> saveUploadedFiles(List<Part> parts, String uploadRealPath) {
        List<PropertyImage> images = new ArrayList<>();
        if (parts == null) return images;

        File uploadDir = new File(uploadRealPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        for (Part part : parts) {
            if (part != null && part.getSize() > 0 && part.getSubmittedFileName() != null) {
                String originalFilename = part.getSubmittedFileName();
                String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFileName = UUID.randomUUID().toString() + fileExt;

                File fileToSave = new File(uploadDir, newFileName);
                try (InputStream input = part.getInputStream()) {
                    Files.copy(input, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    PropertyImage img = new PropertyImage();
                    img.setImagePath("uploads/properties/" + newFileName);
                    images.add(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return images;
    }
}