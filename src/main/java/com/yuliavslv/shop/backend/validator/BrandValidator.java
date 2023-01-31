package com.yuliavslv.shop.backend.validator;

import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@RequiredArgsConstructor
public class BrandValidator {
    private final BrandRepo brandRepo;
    private final Properties properties;

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
            throw new IllegalArgumentException(properties.getProperty("brand.name.nullOrEmpty"));
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException(properties.getProperty("brand.name.longerThan255"));
        }
        if (brandRepo.existsBrandByName(name)) {
            throw new IllegalArgumentException(properties.getProperty("brand.name.alreadyExist"));
        }
    }
}
