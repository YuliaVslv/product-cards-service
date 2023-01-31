package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.service.BrandService;
import com.yuliavslv.shop.backend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;
    private final ProductService productService;

    public BrandController(BrandService brandService, ProductService productService) {
        this.brandService = brandService;
        this.productService = productService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{brandName}")
    public List<Product> getProductsInBrand(@PathVariable("brandName") String brandName) {
        return productService.getAllByBrand(brandName);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Brand> getAll() {
        return brandService.getAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Brand add(@RequestBody Brand brand) {
        return brandService.add(brand);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{brandName}")
    public void delete(@PathVariable("brandName") String brandName) {
        brandService.delete(brandName);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{brandName}")
    public Brand change(@PathVariable("brandName") String brandName, @RequestBody Brand changes) {
        return brandService.change(brandName, changes);

    }
}
