package com.yuliavslv.shop.backend.service;

import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.ProductTypeRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductTypeService {
    private final ProductTypeRepo productTypeRepo;

    public ProductTypeService(ProductTypeRepo productTypeRepo) {
        this.productTypeRepo = productTypeRepo;
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
        return productTypeRepo.save(productType);
    }

    public void delete(String productTypeName)
            throws NoSuchElementException, DataIntegrityViolationException {
        ProductType productType = getByName(productTypeName);
        productTypeRepo.delete(productType);
    }

    public ProductType change(String productTypeName, ProductType changes)
            throws NoSuchElementException {
        ProductType productType = getByName(productTypeName);
        if (changes.getName() != null) {
            productType.setName(changes.getName());
            productTypeRepo.save(productType);
        }
        return productType;
    }
}
