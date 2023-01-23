package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.dto.ProductDto;
import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import com.yuliavslv.shop.backend.repo.ProductRepo;
import com.yuliavslv.shop.backend.repo.ProductTypeRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepo productRepo;
    private final BrandRepo brandRepo;
    private final ProductTypeRepo productTypeRepo;

    public ProductController(ProductRepo productRepo, BrandRepo brandRepo, ProductTypeRepo productTypeRepo) {
        this.productRepo = productRepo;
        this.brandRepo = brandRepo;
        this.productTypeRepo = productTypeRepo;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @PostMapping("/add_product")
    public Integer addProduct(@RequestBody ProductDto productDto) {
        Optional<Brand> brand = brandRepo.findById(productDto.getBrandId());
        if (!brand.isPresent()) {
            return null;
        }
        Optional<ProductType> type = productTypeRepo.findById(productDto.getTypeId());
        if (!type.isPresent()) {
            return null;
        }
        Product result = productRepo.save(new Product(
                brand.get(),
                productDto.getName(),
                type.get(),
                productDto.getPrice(),
                productDto.getSale(),
                productDto.getAmount()));
        return result.getId();
    }
}
