package com.qlbds.service;

import com.qlbds.dto.user.ViewHistoryDTO; // Import package mới
import com.qlbds.entity.Property;
import com.qlbds.entity.User;
import com.qlbds.entity.ViewLog;
import com.qlbds.repository.ViewLogRepository;

import java.util.ArrayList;
import java.util.List;

public class ViewLogService {
    private ViewLogRepository viewLogRepo = new ViewLogRepository();

    public void logPropertyView(Integer userId, Integer propertyId) {
        viewLogRepo.deleteOldLog(userId, propertyId);

        ViewLog log = new ViewLog();
        User user = new User();
        user.setId(userId);
        Property property = new Property();
        property.setId(propertyId);

        log.setUser(user);
        log.setProperty(property);

        viewLogRepo.saveLog(log);
    }

    public List<ViewHistoryDTO> getViewHistory(Integer userId) {
        List<ViewLog> logs = viewLogRepo.findLogsByUserId(userId);
        List<ViewHistoryDTO> dtos = new ArrayList<>();

        for (ViewLog log : logs) {
            ViewHistoryDTO dto = new ViewHistoryDTO();
            Property p = log.getProperty();

            dto.setPropertyId(p.getId());
            dto.setTitle(p.getTitle());
            dto.setAddress(p.getAddress());
            dto.setPrice(p.getPrice());
            dto.setArea(p.getArea());

            // Fix an toàn bảo vệ lỗi NullPointerException
            dto.setPropertyType(p.getPropertyType() != null ? p.getPropertyType().name() : "");

            // Lấy ảnh đầu tiên làm Thumbnail hiển thị gọn nhẹ
            if (p.getImages() != null && !p.getImages().isEmpty()) {
                dto.setThumbnail(p.getImages().get(0).getImagePath());
            } else {
                dto.setThumbnail("assets/customer/img/property-1.jpg"); // Ảnh mặc định nếu BĐS không có ảnh
            }

            dtos.add(dto);
        }
        return dtos;
    }
}