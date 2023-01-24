package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.dto.AppError;
import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import com.yuliavslv.shop.backend.repo.ProductRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandRepo brandRepo;
    private final ProductRepo productRepo;

    public BrandController(BrandRepo brandRepo, ProductRepo productRepo) {
        this.brandRepo = brandRepo;
        this.productRepo = productRepo;
    }

    @GetMapping("/{brandName}")
    public ResponseEntity<?> getProductsInBrand(@PathVariable("brandName") String brandName) {
        try {
            Brand brand = brandRepo.findBrandByName(brandName).orElseThrow();
            List<Product> result = productRepo.findByBrand_Id(brand.getId());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            "The specified brand does not exist"),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping
    public List<Brand> getAllBrands() {
        return brandRepo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> addBrand(@RequestBody Brand brand) {
        Brand result = brandRepo.save(brand);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
