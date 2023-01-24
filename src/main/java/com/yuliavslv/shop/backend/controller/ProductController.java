package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.dto.AppError;
import com.yuliavslv.shop.backend.dto.IdDto;
import com.yuliavslv.shop.backend.dto.ProductDto;
import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import com.yuliavslv.shop.backend.repo.ProductRepo;
import com.yuliavslv.shop.backend.repo.ProductTypeRepo;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Integer id) {
        try {
            Product result = productRepo.findById(id).orElseThrow();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            "Product with given id does not exist"
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    //TO DO: handle missing data error
    @PostMapping
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
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            "The brand or type of product with the given id was not found"),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    //TO DO: move processing DataIntegrityViolationException error to a separate method
    @DeleteMapping
    public ResponseEntity<?> deleteProduct(@RequestBody IdDto idDto) {
        if (productRepo.existsById(idDto.getId())) {
            try {
                productRepo.deleteById(idDto.getId());
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
                            "Product with given id does not exist"
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
    }
}
