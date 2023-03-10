package com.yuliavslv.shop.backend.service;

import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import com.yuliavslv.shop.backend.validator.BrandValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepo brandRepo;
    private final BrandValidator brandValidator;
    private final Properties properties;

    public List<Brand> getAll() {
        return brandRepo.findAll();
    }

    public Brand getById(Integer brandId)
            throws NoSuchElementException {
        return brandRepo.findById(brandId).
                orElseThrow(()->new NoSuchElementException(properties.getProperty("brand.id.notExist")));
    }

    public Brand getByName(String brandName)
            throws NoSuchElementException {
        return brandRepo.findBrandByName(brandName).
                orElseThrow(()->new NoSuchElementException(properties.getProperty("brand.name.notExist")));
    }

    public Brand add(Brand brand) {
        brandValidator.validateBrand(brand);
        return brandRepo.save(brand);
    }

    public void delete(String brandName)
            throws NoSuchElementException, DataIntegrityViolationException {
        Brand brand = getByName(brandName);
        brandRepo.delete(brand);
    }

    public Brand change(String brandName, Brand changes)
            throws NoSuchElementException, IllegalArgumentException {
        Brand brand = getByName(brandName);
        if (changes.getName() != null) {
            brandValidator.validateName(changes.getName());
            brand.setName(changes.getName());
            brandRepo.updateBrandName(brand.getId(), changes.getName());
        }
        return brand;
    }
}
