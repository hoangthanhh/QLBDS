package com.qlbds.dto.property;

import java.math.BigDecimal;
import java.util.List;

public class PropertyDetailDTO {
    private Integer id;
    private String title;
    private String address;
    private Long price;
    private BigDecimal area;
    private String propertyType;
    private String status;
    private String description;
    private List<String> imageUrls; // Chuyển Entity List thành String List

    public PropertyDetailDTO() {}

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
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
}