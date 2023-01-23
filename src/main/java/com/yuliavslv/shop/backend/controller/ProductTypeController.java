package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.ProductTypeRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class ProductTypeController {

    ProductTypeRepo productTypeRepo;

    public ProductTypeController(ProductTypeRepo productTypeRepo) {
        this.productTypeRepo = productTypeRepo;
    }

    @GetMapping
    public List<ProductType> getAllCategories() {
        return productTypeRepo.findAll();
    }

    @PostMapping("add_category")
    public Integer addBrand(String name) {
        ProductType result = productTypeRepo.save(new ProductType(name));
        return result.getId();
    }
}
