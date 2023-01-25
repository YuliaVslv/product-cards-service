package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.dto.AppError;
import com.yuliavslv.shop.backend.dto.IdDto;
import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import com.yuliavslv.shop.backend.repo.ProductRepo;
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
    public ResponseEntity<?> addBrand(@RequestBody @Valid Brand brand) {
        Brand result = brandRepo.save(brand);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    //TODO: move processing DataIntegrityViolationException error to a separate method
    @DeleteMapping
    public ResponseEntity<?> deleteBrand(@RequestBody @Valid IdDto idDto) {
        if (brandRepo.existsById(idDto.getId())) {
            try {
                brandRepo.deleteById(idDto.getId());
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
                            "Brand with given id does not exist"
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
    }

    @PutMapping("/{brandName}")
    public ResponseEntity<?> changeBrand(@PathVariable("brandName") String brandName, @RequestBody Brand changes) {
        try {
            Brand brand = brandRepo.findBrandByName(brandName).orElseThrow();
            if (changes.getName() != null) {
                brand.setName(changes.getName());
                brandRepo.save(brand);
            }
            return new ResponseEntity<>(brand, HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            "Brand with given name does not exist"
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }

    }
}
