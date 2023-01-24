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

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandRepo brandRepo;
    private final ProductRepo productRepo;

    public BrandController(BrandRepo brandRepo, ProductRepo productRepo) {
        this.brandRepo = brandRepo;
        this.productRepo = productRepo;
    }

    @GetMapping
    public ResponseEntity<?> getProductsInBrand(@RequestParam Integer id) {
        if (brandRepo.existsById(id)) {
            List<Product> result = productRepo.findByBrand_Id(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            "Brand with given id does not exist"),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/all")
    public List<Brand> getAllBrands() {
        return brandRepo.findAll();
    }

    @PostMapping("/add_brand")
    public ResponseEntity<?> addBrand(@RequestBody Brand brand) {
        Brand result = brandRepo.save(brand);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
