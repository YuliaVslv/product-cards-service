package com.yuliavslv.shop.backend.validator;

import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.ProductTypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@RequiredArgsConstructor
public class ProductTypeValidator {
    private final ProductTypeRepo productTypeRepo;
    private final Properties properties;

    public boolean isExistById(Integer id) {
        return productTypeRepo.existsById(id);
    }

    public void validateProductType(ProductType productType)
            throws IllegalArgumentException {
        validateName(productType.getName());
    }

    public void validateName(String name)
            throws IllegalArgumentException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(properties.getProperty("productType.name.nullOrEmpty"));
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException(properties.getProperty("productType.name.longerThan255"));
        }
        if (productTypeRepo.existsProductTypeByName(name)) {
            throw new IllegalArgumentException(properties.getProperty("productType.name.alreadyExist"));
        }
    }
}
