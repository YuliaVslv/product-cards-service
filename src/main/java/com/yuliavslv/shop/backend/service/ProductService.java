package com.yuliavslv.shop.backend.service;

import com.yuliavslv.shop.backend.dto.ProductDto;
import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.ProductRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    private final ProductRepo productRepo;
    private final BrandService brandService;
    private final ProductTypeService productTypeService;

    public ProductService(ProductRepo productRepo, BrandService brandService, ProductTypeService productTypeService) {
        this.productRepo = productRepo;
        this.brandService = brandService;
        this.productTypeService = productTypeService;
    }

    public List<Product> getAll() {
        return productRepo.findAll();
    }

    public Product getById(Integer productId)
            throws NoSuchElementException {
        return productRepo.findById(productId).orElseThrow();
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
            throws NoSuchElementException {
        Brand brand = brandService.getById(product.getBrandId());
        ProductType productType = productTypeService.getById(product.getTypeId());
        return productRepo.save(new Product(
                brand,
                product.getName(),
                productType,
                product.getPrice(),
                product.getSale(),
                product.getAmount()
        ));
    }

    public void delete(Integer productId)
            throws EmptyResultDataAccessException, DataIntegrityViolationException {
        productRepo.deleteById(productId);
    }

    public Product change(Integer productId, ProductDto changes)
            throws NoSuchElementException {
        Product product = productRepo.findById(productId).orElseThrow();
        if (changes.getBrandId() != null) {
            product.setBrand(brandService.getById(changes.getBrandId()));
        }
        if (changes.getTypeId() != null) {
            product.setType(productTypeService.getById(changes.getTypeId()));
        }
        if (changes.getName() != null) {
            product.setName(changes.getName());
        }
        if (changes.getPrice() != null) {
            product.setPrice(changes.getPrice());
        }
        if (changes.getSale() != null) {
            product.setSale(changes.getSale());
        }
        if (changes.getAmount() != null) {
            product.setAmount(changes.getAmount());
        }
        return productRepo.save(product);
    }
}
