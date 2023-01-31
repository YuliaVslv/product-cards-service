package com.yuliavslv.shop.backend.validator;

import com.yuliavslv.shop.backend.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {
    private final BrandValidator brandValidator;
    private final ProductTypeValidator productTypeValidator;

    public ProductValidator(BrandValidator brandValidator, ProductTypeValidator productTypeValidator) {
        this.brandValidator = brandValidator;
        this.productTypeValidator = productTypeValidator;
    }

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
            throw new IllegalArgumentException("Brand with given id does not exist");
        }
    }

    public void validateProductTypeId(Integer id)
            throws IllegalArgumentException {
        if (!productTypeValidator.isExistById(id)) {
            throw new IllegalArgumentException("Category with given id does not exist");
        }
    }

    public void validateName(String name)
            throws IllegalArgumentException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Category name must be less than 255 character long");
        }
    }

    public void validatePrice(Double price)
            throws IllegalArgumentException {
        if (price < 0) {
            throw new IllegalArgumentException("Price must be greater than or equal to zero");
        }
    }

    public void validateDiscount(Integer discount)
            throws IllegalArgumentException {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
    }

    public void validateAmount(Integer amount)
            throws IllegalArgumentException {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be greater than or equal to zero");
        }
    }
}
