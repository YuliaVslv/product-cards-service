package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.dto.AppError;
import com.yuliavslv.shop.backend.dto.IdDto;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.ProductRepo;
import com.yuliavslv.shop.backend.repo.ProductTypeRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<?> addBrand(@RequestBody @Valid ProductType productType) {
        ProductType result = productTypeRepo.save(productType);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    //TODO: move processing DataIntegrityViolationException error to a separate method
    @DeleteMapping
    public ResponseEntity<?> deleteProductType(@RequestBody @Valid IdDto idDto) {
        if (productTypeRepo.existsById(idDto.getId())) {
            try {
                productTypeRepo.deleteById(idDto.getId());
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            } catch (DataIntegrityViolationException e) {
                String message = e.getCause().getCause().getMessage();
                return new ResponseEntity<>(
                        new AppError(
                                HttpStatus.CONFLICT.value(),
                                message
                        ),
                        HttpStatus.CONFLICT
                );
            }
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

    @PutMapping("/{typeName}")
    public ResponseEntity<?> changeCategory(@PathVariable("typeName") String typeName, @RequestBody ProductType changes) {
        try {
            ProductType productType = productTypeRepo.findProductTypeByName(typeName).orElseThrow();
            if (changes.getName() != null) {
                productType.setName(changes.getName());
                productTypeRepo.save(productType);
            }
            return new ResponseEntity<>(productType, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            "Category with given name does not exist"
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
    }
}
