package com.yuliavslv.shop.backend.validator;

import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.ProductTypeRepo;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeValidator {
    private final ProductTypeRepo productTypeRepo;

    public ProductTypeValidator(ProductTypeRepo productTypeRepo) {
        this.productTypeRepo = productTypeRepo;
    }

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
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Category name must be less than 255 characters long");
        }
        if (!productTypeRepo.existsProductTypeByName(name)) {
            throw new IllegalArgumentException("A category with this name already exists");
        }
    }
}
