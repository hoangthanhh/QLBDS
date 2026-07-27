package com.qlbds.controller.customer;

import java.util.ArrayList;
import java.util.List;

public class MockProperty {
    private int id;
    private String title;
    private String address;
    private String propertyType;
    private long price;
    private String status;
    private List<MockImage> images;

    public MockProperty(int id, String title, String address, String propertyType, long price, String status, String defaultImageUrl) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.propertyType = propertyType;
        this.price = price;
        this.status = status;
        this.images = new ArrayList<>();
        if (!defaultImageUrl.isEmpty()) {
            this.images.add(new MockImage(defaultImageUrl));
        }
    }

    // Các hàm Getter cần thiết để JSTL EL (${item.xxx}) có thể đọc được dữ liệu
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAddress() { return address; }
    public String getPropertyType() { return propertyType; }
    public long getPrice() { return price; }
    public String getStatus() { return status; }
    public List<MockImage> getImages() { return images; }

    // Inner class để chứa ảnh
    public static class MockImage {
        private String imagePath;
        public MockImage(String imagePath) { this.imagePath = imagePath; }
        public String getImagePath() { return imagePath; }
    }
}