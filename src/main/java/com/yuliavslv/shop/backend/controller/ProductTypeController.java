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

@RestController
@RequestMapping("/categories")
public class ProductTypeController {

    ProductTypeRepo productTypeRepo;
    ProductRepo productRepo;

    public ProductTypeController(ProductTypeRepo productTypeRepo, ProductRepo productRepo) {
        this.productRepo = productRepo;
        this.productTypeRepo = productTypeRepo;
    }

    @GetMapping
    public ResponseEntity<?> getProductsInCategory(@RequestParam Integer id) {
        if (productTypeRepo.existsById(id)) {
            List<Product> result = productRepo.findByType_Id(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            "Category with given id does not exist"
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
    }

    @GetMapping("/all")
    public List<ProductType> getAllCategories() {
        return productTypeRepo.findAll();
    }

    @PostMapping("/add_category")
    public ResponseEntity<?> addBrand(@RequestBody ProductType productType) {
        ProductType result = productTypeRepo.save(productType);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
