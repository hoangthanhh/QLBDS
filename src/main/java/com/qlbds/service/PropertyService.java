package com.qlbds.service;

import com.qlbds.dto.property.PropertyDetailDTO;
import com.qlbds.dto.property.PropertySummaryDTO;
import com.qlbds.entity.Property;
import com.qlbds.entity.PropertyImage;
import com.qlbds.repository.PropertyRepository;

import java.util.ArrayList;
import java.util.List;

public class PropertyService {

    private PropertyRepository propertyRepository = new PropertyRepository();

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
                dto.setThumbnail(p.getImages().get(0).getImagePath()); // Lấy ảnh đầu tiên
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
}