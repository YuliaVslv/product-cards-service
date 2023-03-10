package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.service.ProductService;
import com.yuliavslv.shop.backend.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class ProductTypeController {

    private final ProductTypeService productTypeService;
    private final ProductService productService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{categoryName}")
    public List<Product> getProductsInCategory(@PathVariable("categoryName") String productTypeName) {
        return productService.getAllByProductType(productTypeName);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ProductType> getAll() {
        return productTypeService.getAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductType add(@RequestBody ProductType productType) {
        return productTypeService.add(productType);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{categoryName}")
    public void delete(@PathVariable("categoryName") String productTypeName) {
        productTypeService.delete(productTypeName);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{categoryName}")
    public ProductType change(@PathVariable("categoryName") String productTypeName, @RequestBody ProductType changes) {
        return productTypeService.change(productTypeName,changes);
    }
}
