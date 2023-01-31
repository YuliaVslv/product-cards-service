package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.dto.ProductDto;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final Properties properties;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{productId}")
    public Product get(@PathVariable("productId") Integer productId) {
            return productService.getById(productId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Product add(@RequestBody ProductDto product) {
        return productService.add(product);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{productId}")
    public void delete(@PathVariable("productId") Integer productId) {
        productService.delete(productId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{productId}")
    public Product change(@PathVariable("productId") Integer productId, @RequestBody ProductDto changes) {
        return productService.change(productId, changes);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/discounts")
    public List<Product> getDiscounts() {
        return productService.getDiscounts();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/discounts/brand")
    public String setDiscountForBrand(@RequestBody ProductDto changes) {
        Integer result = productService.setDiscountForBrand(changes);
        return properties.getProperty("product.update") + result;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/discounts/category")
    public String setDiscountForProductType(@RequestBody ProductDto changes) {
        Integer result = productService.setDiscountForProductType(changes);
        return properties.getProperty("product.update") + result;
    }
}
