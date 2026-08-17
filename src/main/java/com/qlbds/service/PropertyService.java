package com.qlbds.service;

import com.qlbds.constant.PropertyStatusEnum;
import com.qlbds.constant.PropertyTypeEnum;
import com.qlbds.dto.property.PropertyDetailDTO;
import com.qlbds.dto.property.PropertySaveDTO;
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

    private final PropertyRepository propertyRepository = new PropertyRepository();

    // CUSTOMER
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
        if (p.getImages() != null) {
            for (PropertyImage img : p.getImages()) {
                imageUrls.add(img.getImagePath());
            }
        }
        dto.setImageUrls(imageUrls);

        return dto;
    }

    public int getTotalPages(int pageSize, String address, String priceRange, String propertyType) {
        if (pageSize <= 0) pageSize = 6;
        long totalRecords = propertyRepository.countAvailableProperties(address, priceRange, propertyType);
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    // ADMIN
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

    public String deleteProperty(Integer id) {
        Property p = propertyRepository.findById(id);
        if (p == null) return "Bất động sản không tồn tại!";

        if (propertyRepository.hasTransactions(id)) {
            return "Không thể xóa! Bất động sản này đã phát sinh giao dịch đặt cọc hoặc mua bán.";
        }

        return propertyRepository.deleteProperty(id) ? "SUCCESS" : "Lỗi hệ thống khi xóa BĐS!";
    }

    // Lưu tối đa 5 file ảnh
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
}