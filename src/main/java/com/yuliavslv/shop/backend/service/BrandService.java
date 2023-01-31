package com.yuliavslv.shop.backend.service;

import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import com.yuliavslv.shop.backend.validator.BrandValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {
    private final BrandRepo brandRepo;
    private final BrandValidator brandValidator;

    public BrandService(BrandRepo brandRepo, BrandValidator brandValidator) {
        this.brandRepo = brandRepo;
        this.brandValidator = brandValidator;
    }

    public List<Brand> getAll() {
        return brandRepo.findAll();
    }

    public Brand getById(Integer brandId)
            throws NoSuchElementException {
        return brandRepo.findById(brandId).
                orElseThrow(()->new NoSuchElementException("Brand with given id does not exist"));
    }

    public Brand getByName(String brandName)
            throws NoSuchElementException {
        return brandRepo.findBrandByName(brandName).
                orElseThrow(()->new NoSuchElementException("Brand with given name does not exist"));
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
            brandRepo.save(brand);
        }
        return brand;
    }
}
