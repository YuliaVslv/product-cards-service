package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandRepo brandRepo;

    public BrandController(BrandRepo brandRepo) {
        this.brandRepo = brandRepo;
    }

    @GetMapping
    public List<Brand> getAllBrands() {
        return brandRepo.findAll();
    }

    @PostMapping("/add_brand")
    public Integer addBrand(@RequestBody Brand brand) {
        Brand result = brandRepo.save(brand);
        return result.getId();
    }
}
