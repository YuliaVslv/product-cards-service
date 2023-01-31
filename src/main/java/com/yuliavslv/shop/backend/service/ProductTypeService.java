package com.yuliavslv.shop.backend.service;

import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.ProductTypeRepo;
import com.yuliavslv.shop.backend.validator.ProductTypeValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductTypeService {
    private final ProductTypeRepo productTypeRepo;

    private final ProductTypeValidator productTypeValidator;

    public ProductTypeService(ProductTypeRepo productTypeRepo, ProductTypeValidator productTypeValidator) {
        this.productTypeRepo = productTypeRepo;
        this.productTypeValidator = productTypeValidator;
    }

    public List<ProductType> getAll() {
        return productTypeRepo.findAll();
    }

    public ProductType getById(Integer productTypeId)
            throws NoSuchElementException {
        return productTypeRepo.findById(productTypeId).
                orElseThrow(()->new NoSuchElementException("Category with given id does not exist"));
    }

    public ProductType getByName(String productTypeName)
            throws NoSuchElementException {
        return productTypeRepo.findProductTypeByName(productTypeName).
                orElseThrow(()->new NoSuchElementException("Category with given name does not exist"));
    }

    public ProductType add(ProductType productType) {
        productTypeValidator.validateProductType(productType);
        return productTypeRepo.save(productType);
    }

    public void delete(String productTypeName)
            throws NoSuchElementException, DataIntegrityViolationException {
        ProductType productType = getByName(productTypeName);
        productTypeRepo.delete(productType);
    }

    public ProductType change(String productTypeName, ProductType changes)
            throws NoSuchElementException, IllegalArgumentException {
        ProductType productType = getByName(productTypeName);
        if (changes.getName() != null) {
            productTypeValidator.validateName(changes.getName());
            productType.setName(changes.getName());
            productTypeRepo.updateProductTypeName(productType.getId(), changes.getName());
        }
        return productType;
    }
}
