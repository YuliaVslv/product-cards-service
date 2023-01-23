package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private BrandRepo brandRepo;

    public BrandController(BrandRepo brandRepo) {
        this.brandRepo = brandRepo;
    }

    @GetMapping
    public List<Brand> getAllBrands() {
        return brandRepo.findAll();
    }

    @PostMapping("/add_brand")
    public Integer addBrand(String name) {
        Brand result = brandRepo.save(new Brand(name));
        return result.getId();
    }
}
