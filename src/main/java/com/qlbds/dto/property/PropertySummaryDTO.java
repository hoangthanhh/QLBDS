package com.qlbds.dto.property;

import java.math.BigDecimal;

public class PropertySummaryDTO {
    private Integer id;
    private String title;
    private String address;
    private Long price;
    private BigDecimal area;
    private String propertyType;
    private String status;
    private String thumbnail; // Chỉ lưu duy nhất 1 URL ảnh

    public PropertySummaryDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }
    public BigDecimal getArea() { return area; }
    public void setArea(BigDecimal area) { this.area = area; }
    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
}