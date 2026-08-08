package com.qlbds.service;

import com.qlbds.entity.Property;
import com.qlbds.repository.PropertyRepository;
import java.util.List;

public class PropertyService {

    private PropertyRepository propertyRepository = new PropertyRepository();

    public List<Property> getPropertiesByPage(int page, int pageSize, String address, String priceRange, String propertyType) {
        return propertyRepository.findAllAvailableByPage(page, pageSize, address, priceRange, propertyType);
    }

    public int getTotalPages(int pageSize, String address, String priceRange, String propertyType) {
        long totalRecords = propertyRepository.countAvailableProperties(address, priceRange, propertyType);
        return (int) Math.ceil((double) totalRecords / pageSize);
    }
}