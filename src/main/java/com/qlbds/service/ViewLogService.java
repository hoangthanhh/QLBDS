package com.qlbds.service;

import com.qlbds.constant.RoleTypeEnum;
import com.qlbds.dto.admin.AdminViewLogDTO;
import com.qlbds.dto.user.ViewHistoryDTO;
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

    // ĐÃ SỬA: Truyền thêm tham số page, pageSize xuống Repository
    public List<ViewHistoryDTO> getViewHistory(Integer userId, int page, int pageSize) {
        List<ViewLog> logs = viewLogRepo.findLogsByUserId(userId, page, pageSize);
        List<ViewHistoryDTO> dtos = new ArrayList<>();

        for (ViewLog log : logs) {
            ViewHistoryDTO dto = new ViewHistoryDTO();
            Property p = log.getProperty();

            dto.setPropertyId(p.getId());
            dto.setTitle(p.getTitle());
            dto.setAddress(p.getAddress());
            dto.setPrice(p.getPrice());
            dto.setArea(p.getArea());
            dto.setPropertyType(p.getPropertyType() != null ? p.getPropertyType().name() : "");

            if (p.getImages() != null && !p.getImages().isEmpty()) {
                dto.setThumbnail(p.getImages().get(0).getImagePath());
            } else {
                dto.setThumbnail("assets/customer/img/property-1.jpg");
            }

            dtos.add(dto);
        }
        return dtos;
    }

    public List<AdminViewLogDTO> getLogsForAdmin(int page, int pageSize) {
        // Truyền RoleTypeEnum.CUSTOMER (hoặc USER) vào để chỉ lấy log của khách hàng
        List<ViewLog> logs = viewLogRepo.findAllLogsForAdmin(RoleTypeEnum.CUSTOMER, page, pageSize);
        List<AdminViewLogDTO> dtos = new ArrayList<>();

        for (ViewLog log : logs) {
            AdminViewLogDTO dto = new AdminViewLogDTO();
            dto.setLogId(log.getId());
            dto.setUserId(log.getUser().getId());
            dto.setCustomerName(log.getUser().getFullName());
            dto.setCustomerEmail(log.getUser().getEmail());
            dto.setPropertyId(log.getProperty().getId());
            dto.setPropertyTitle(log.getProperty().getTitle());
            dto.setViewedAt(log.getViewedAt());
            dtos.add(dto);
        }
        return dtos;
    }

    public int getTotalPagesForAdmin(int pageSize) {
        // Truyền RoleTypeEnum.CUSTOMER (hoặc USER) vào để đếm số trang chuẩn xác
        long totalRecords = viewLogRepo.countAllLogs(RoleTypeEnum.CUSTOMER);
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    // THÊM MỚI: Hàm tính tổng số trang dựa trên pageSize
    public int getTotalPages(Integer userId, int pageSize) {
        long totalRecords = viewLogRepo.countLogsByUserId(userId);
        return (int) Math.ceil((double) totalRecords / pageSize);
    }
}