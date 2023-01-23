package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import com.yuliavslv.shop.backend.repo.ProductRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandRepo brandRepo;
    private final ProductRepo productRepo;

    public BrandController(BrandRepo brandRepo, ProductRepo productRepo) {
        this.brandRepo = brandRepo;
        this.productRepo = productRepo;
    }

    @GetMapping("/all")
    public List<Brand> getAllBrands() {
        return brandRepo.findAll();
    }

    @GetMapping
    public List<Product> getProductsInBrand(@RequestParam Integer id) {
        return productRepo.findByBrand_Id(id);
    }

    @PostMapping("/add_brand")
    public Integer addBrand(@RequestBody Brand brand) {
        Brand result = brandRepo.save(brand);
        return result.getId();
    }
}
