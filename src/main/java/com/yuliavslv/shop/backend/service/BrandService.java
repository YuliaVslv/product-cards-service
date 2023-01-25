package com.yuliavslv.shop.backend.service;

import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.NoSuchElementException;

public class BrandService {
    private final BrandRepo brandRepo;

    public BrandService(BrandRepo brandRepo) {
        this.brandRepo = brandRepo;
    }

    public List<Brand> getAll() {
        return brandRepo.findAll();
    }

    public Brand getByName(String brandName)
            throws NoSuchElementException {
        return brandRepo.findBrandByName(brandName).orElseThrow();
    }

    public Brand add(Brand brand) {
        return brandRepo.save(brand);
    }

    public void delete(String brandName)
            throws NoSuchElementException, DataIntegrityViolationException {
        Brand brand = brandRepo.findBrandByName(brandName).orElseThrow();
        brandRepo.delete(brand);
    }

    public Brand change(String brandName, Brand changes)
            throws NoSuchElementException {
        Brand brand = brandRepo.findBrandByName(brandName).orElseThrow();
        if (changes.getName() != null) {
            brand.setName(changes.getName());
            brandRepo.save(brand);
        }
        return brand;
    }
}
