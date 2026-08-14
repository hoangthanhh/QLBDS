package com.qlbds.dto.property;

import javax.servlet.http.Part;
import java.util.List;

public class PropertyCreateUpdateDTO {
    private Integer id;
    private String title;
    private String address;
    private String propertyType;
    private Double price;
    private Double area;
    private String description;
    private List<Part> imageParts;

    public PropertyCreateUpdateDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Part> getImageParts() {
        return imageParts;
    }

    public void setImageParts(List<Part> imageParts) {
        this.imageParts = imageParts;
    }
}