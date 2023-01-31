package com.yuliavslv.shop.backend.validator;

import com.yuliavslv.shop.backend.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@RequiredArgsConstructor
public class ProductValidator {
    private final BrandValidator brandValidator;
    private final ProductTypeValidator productTypeValidator;
    private final Properties properties;

    public void validateProduct(ProductDto product)
            throws IllegalArgumentException {
        validateName(product.getName());
        validatePrice(product.getPrice());
        validateDiscount(product.getDiscount());
        validateAmount(product.getAmount());
    }

    public void validateBrandId(Integer id)
            throws IllegalArgumentException {
        if (!brandValidator.isExistById(id)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateProductTypeId(Integer id)
            throws IllegalArgumentException {
        if (!productTypeValidator.isExistById(id)) {
            throw new IllegalArgumentException(properties.getProperty("productType.id.notExist"));
        }
    }

    public void validateName(String name)
            throws IllegalArgumentException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(properties.getProperty("product.name.nullOrEmpty"));
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException(properties.getProperty("product.name.longerThan255"));
        }
    }

    public void validatePrice(Double price)
            throws IllegalArgumentException {
        if (price < 0) {
            throw new IllegalArgumentException(properties.getProperty("product.price.negative"));
        }
    }

    public void validateDiscount(Integer discount)
            throws IllegalArgumentException {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException(properties.getProperty("product.discount.outOfRange"));
        }
    }

    public void validateAmount(Integer amount)
            throws IllegalArgumentException {
        if (amount < 0) {
            throw new IllegalArgumentException(properties.getProperty("product.amount.negative"));
        }
    }
}
