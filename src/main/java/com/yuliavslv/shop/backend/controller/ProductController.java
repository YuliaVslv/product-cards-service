package com.yuliavslv.shop.backend.controller;

import com.yuliavslv.shop.backend.dto.AppError;
import com.yuliavslv.shop.backend.dto.ProductDto;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.service.ProductService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> get(@PathVariable("productId") Integer productId) {
        try {
            Product result = productService.getById(productId);
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
    public List<Product> getAll() {
        return productService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid ProductDto product) {
        try {
            Product result = productService.add(product);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            "The brand or type of product with the given id was not found"),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    //TODO: move processing DataIntegrityViolationException error to a separate method
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> delete(@PathVariable("productId") Integer productId) {
        try {
            productService.delete(productId);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            "Product with given id does not exist"
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

    @PutMapping("/{productId}")
    public ResponseEntity<?> change(@PathVariable("productId") Integer productId, @RequestBody ProductDto changes) {
        try {
            Product result = productService.change(productId, changes);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    new AppError(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            "Changes cannot be made because a product, brand, or category with the given id does not exist."
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
    }

    @GetMapping("/discounts")
    public ResponseEntity<?> getDiscounts() {
        List<Product> result = productService.getDiscounts();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
