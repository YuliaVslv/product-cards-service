package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.dto.AppError;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.ProductRepo;
import com.yuliavslv.shop.backend.repo.ProductTypeRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/categories")
public class ProductTypeController {

    ProductTypeRepo productTypeRepo;
    ProductRepo productRepo;

    public ProductTypeController(ProductTypeRepo productTypeRepo, ProductRepo productRepo) {
        this.productRepo = productRepo;
        this.productTypeRepo = productTypeRepo;
    }

    @GetMapping("/{typeName}")
    public ResponseEntity<?> getProductsInCategory(@PathVariable("typeName") String typeName) {
        try {
            ProductType productType = productTypeRepo.findProductTypeByName(typeName).orElseThrow();
            List<Product> result = productRepo.findByType_Id(productType.getId());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            "The specified category does not exist"
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
    }

    @GetMapping
    public List<ProductType> getAllCategories() {
        return productTypeRepo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> addBrand(@RequestBody ProductType productType) {
        ProductType result = productTypeRepo.save(productType);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
