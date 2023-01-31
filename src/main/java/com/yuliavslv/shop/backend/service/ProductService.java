package com.yuliavslv.shop.backend.service;

import com.yuliavslv.shop.backend.dto.ProductDto;
import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.ProductRepo;
import com.yuliavslv.shop.backend.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final BrandService brandService;
    private final ProductTypeService productTypeService;
    private final ProductValidator productValidator;
    private final Properties properties;


    public List<Product> getAll() {
        return productRepo.findAll();
    }

    public Product getById(Integer productId)
            throws NoSuchElementException {
        return productRepo.findById(productId).
                orElseThrow(()->new NoSuchElementException(properties.getProperty("product.id.notExist")));
    }

    public List<Product> getAllByBrand(String brandName)
            throws NoSuchElementException {
        Integer brandId = brandService.getByName(brandName).getId();
        return productRepo.findByBrand_Id(brandId);
    }

    public List<Product> getAllByProductType(String productTypeName)
            throws NoSuchElementException {
        Integer productTypeId = productTypeService.getByName(productTypeName).getId();
        return productRepo.findByType_Id(productTypeId);
    }

    public Product add(ProductDto product)
            throws NoSuchElementException, IllegalArgumentException {
        Brand brand = brandService.getById(product.getBrandId());
        ProductType productType = productTypeService.getById(product.getTypeId());
        productValidator.validateProduct(product);
        return productRepo.save(new Product(
                brand,
                product.getName(),
                productType,
                product.getPrice(),
                product.getDiscount(),
                product.getAmount()
        ));
    }

    public void delete(Integer productId)
            throws NoSuchElementException, DataIntegrityViolationException {
        Product product = getById(productId);
        productRepo.delete(product);
    }

    public Product change(Integer productId, ProductDto changes)
            throws NoSuchElementException, IllegalArgumentException {
        Product product = productRepo.findById(productId).orElseThrow();
        if (changes.getBrandId() != null) {
            product.setBrand(brandService.getById(changes.getBrandId()));
        }
        if (changes.getTypeId() != null) {
            product.setType(productTypeService.getById(changes.getTypeId()));
        }
        if (changes.getName() != null ) {
            productValidator.validateName(changes.getName());
            product.setName(changes.getName());
        }
        if (changes.getPrice() != null) {
            productValidator.validatePrice(changes.getPrice());
            product.setPrice(changes.getPrice());
        }
        if (changes.getDiscount() != null) {
            productValidator.validateDiscount(changes.getDiscount());
            product.setDiscount(changes.getDiscount());
        }
        if (changes.getAmount() != null) {
            productValidator.validateAmount(changes.getAmount());
            product.setAmount(changes.getAmount());
        }
        return productRepo.save(product);
    }

    public List<Product> getDiscounts() {
        return productRepo.findByDiscountIsGreaterThan(0);
    }

    public Integer setDiscountForBrand(ProductDto changes)
            throws IllegalArgumentException {
        if (changes.getBrandId() == null) {
            productValidator.validateBrandId(changes.getBrandId());
            throw new IllegalArgumentException(properties.getProperty("product.brandId.notSpecified"));
        }
        if (changes.getDiscount() == null) {
            productValidator.validateDiscount(changes.getDiscount());
            throw new IllegalArgumentException(properties.getProperty("product.discount.notSpecified"));
        }
        return productRepo.updateDiscountForBrand(changes.getBrandId(), changes.getDiscount());
    }

    public Integer setDiscountForProductType(ProductDto changes)
            throws IllegalArgumentException {
        if (changes.getTypeId() == null) {
            productValidator.validateProductTypeId(changes.getTypeId());
            throw new IllegalArgumentException(properties.getProperty("product.typeId.notSpecified"));
        }
        if (changes.getDiscount() == null) {
            productValidator.validateDiscount(changes.getDiscount());
            throw new IllegalArgumentException(properties.getProperty("product.discount.notSpecified"));
        }
        return productRepo.updateDiscountForProductType(changes.getTypeId(), changes.getDiscount());
    }
}