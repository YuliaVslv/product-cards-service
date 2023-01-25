package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.dto.AppError;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.service.ProductService;
import com.yuliavslv.shop.backend.service.ProductTypeService;
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

    private final ProductTypeService productTypeService;
    private final ProductService productService;

    public ProductTypeController(ProductTypeService productTypeService, ProductService productService) {
        this.productTypeService = productTypeService;
        this.productService = productService;
    }

    @GetMapping("/{categoryName}")
    public ResponseEntity<?> getProductsInCategory(@PathVariable("categoryName") String productTypeName) {
        try {
            List<Product> result = productService.getAllByProductType(productTypeName);
            return new ResponseEntity<>(result, HttpStatus.OK);
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

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ProductType> result = productTypeService.getAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid ProductType productType) {
        ProductType result = productTypeService.add(productType);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    //TODO: move processing DataIntegrityViolationException error to a separate method
    @DeleteMapping("/{categoryName}")
    public ResponseEntity<?> delete(@PathVariable("categoryName") String productTypeName) {
        try {
            productTypeService.delete(productTypeName);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            "Category with given name does not exist"
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
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
    }

    @PutMapping("/{categoryName}")
    public ResponseEntity<?> change(@PathVariable("categoryName") String productTypeName, @RequestBody ProductType changes) {
        try {
            ProductType result = productTypeService.change(productTypeName,changes);
            return new ResponseEntity<>(result, HttpStatus.OK);
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
