package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.dto.AppError;
import com.yuliavslv.shop.backend.dto.ProductDto;
import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import com.yuliavslv.shop.backend.repo.ProductRepo;
import com.yuliavslv.shop.backend.repo.ProductTypeRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        try {
            Brand brand = brandRepo.findById(productDto.getBrandId()).orElseThrow();
            ProductType type = productTypeRepo.findById(productDto.getTypeId()).orElseThrow();
            Product result = productRepo.save(new Product(
                    brand,
                    productDto.getName(),
                    type,
                    productDto.getPrice(),
                    productDto.getSale(),
                    productDto.getAmount()));
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.BAD_REQUEST.value(),
                            "The brand or type of product with the given id was not found"),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
