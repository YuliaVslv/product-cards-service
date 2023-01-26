package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.dto.AppError;
import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.service.BrandService;
import com.yuliavslv.shop.backend.service.ProductService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;
    private final ProductService productService;

    public BrandController(BrandService brandService, ProductService productService) {
        this.brandService = brandService;
        this.productService = productService;
    }

    @GetMapping("/{brandName}")
    public ResponseEntity<?> getProductsInBrand(@PathVariable("brandName") String brandName) {
        try {
            List<Product> result = productService.getAllByBrand(brandName);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            e.getMessage()),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping
    public List<Brand> getAll() {
        return brandService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid Brand brand) {
        Brand result = brandService.add(brand);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{brandName}")
    public ResponseEntity<?> delete(@PathVariable("brandName") String brandName) {
        try {
            brandService.delete(brandName);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            e.getMessage()
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.CONFLICT.value(),
                            e.getCause().getMessage()
                    ),
                    HttpStatus.CONFLICT
            );
        }
    }

    @PutMapping("/{brandName}")
    public ResponseEntity<?> change(@PathVariable("brandName") String brandName, @RequestBody Brand changes) {
        try {
            Brand brand = brandService.change(brandName, changes);
            return new ResponseEntity<>(brand, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            e.getMessage()
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }

    }
}
