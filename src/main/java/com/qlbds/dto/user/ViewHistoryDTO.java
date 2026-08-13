package com.qlbds.dto.user;

import java.math.BigDecimal;

public class ViewHistoryDTO {
    private Integer propertyId;
    private String title;
    private String address;
    private Long price;
    private BigDecimal area;
    private String propertyType;
    private String thumbnail; // Lưu đường dẫn ảnh đầu tiên

    public ViewHistoryDTO() {}

    public Integer getPropertyId() { return propertyId; }
    public void setPropertyId(Integer propertyId) { this.propertyId = propertyId; }

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

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
}