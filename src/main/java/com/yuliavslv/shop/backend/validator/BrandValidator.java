package com.yuliavslv.shop.backend.validator;

import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandValidator {
    private final BrandRepo brandRepo;

    public boolean isExistById(Integer id) {
        return brandRepo.existsById(id);
    }

    public void validateBrand(Brand brand)
            throws IllegalArgumentException {
        validateName(brand.getName());
    }

    public void validateName(String name)
            throws IllegalArgumentException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Brand name cannot be null or empty");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Brand name must be less than 255 characters long");
        }
        if (brandRepo.existsBrandByName(name)) {
            throw new IllegalArgumentException("A brand with this name already exists");
        }
    }
}
