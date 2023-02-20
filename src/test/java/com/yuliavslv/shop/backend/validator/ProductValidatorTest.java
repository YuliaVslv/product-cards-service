package com.yuliavslv.shop.backend.validator;

import com.yuliavslv.shop.backend.BackendApplication;
import com.yuliavslv.shop.backend.dto.ProductDto;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Verify ProductValidator class")
class ProductValidatorTest {
    final static String MESSAGES_PROP = "messages_ENG.properties";

    static BrandValidator brandValidatorMock;
    static ProductTypeValidator productTypeValidatorMock;

    static ProductValidator productValidator;
    static Properties properties;

    @BeforeAll
    static void init() {
        properties = new Properties();
        try(InputStream propertiesInputStream = BackendApplication.class.getClassLoader().getResourceAsStream(MESSAGES_PROP)) {
            properties.load(propertiesInputStream);
        } catch (IOException e) {
            Assertions.fail(e);
        }

        brandValidatorMock = Mockito.mock(BrandValidator.class);
        productTypeValidatorMock = Mockito.mock(ProductTypeValidator.class);
        productValidator = new ProductValidator(brandValidatorMock, productTypeValidatorMock, properties);
    }

    @Test
    void validateProduct_correct() {
        ProductDto productDto = new ProductDto(
                1,
                "product",
                1,
                100.0,
                10,
                1000
        );

        Assertions.assertDoesNotThrow(()->productValidator.validateProduct(productDto));
    }

    @Test
    void ValidateProduct_nullName() {
        ProductDto productDto = new ProductDto(
                1,
                null,
                1,
                100.0,
                10,
                1000
        );

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateProduct(productDto)
        );
        Assertions.assertEquals(properties.getProperty("product.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void ValidateProduct_blankName() {
        ProductDto productDto = new ProductDto(
                1,
                " ",
                1,
                100.0,
                10,
                1000
        );

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateProduct(productDto)
        );
        Assertions.assertEquals(properties.getProperty("product.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void ValidateProduct_longName() {
        ProductDto productDto = new ProductDto(
                1,
                "a".repeat(256),
                1,
                100.0,
                10,
                1000
        );

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateProduct(productDto)
        );
        Assertions.assertEquals(properties.getProperty("product.name.longerThan255"), exception.getMessage());
    }

    @Test
    void ValidateProduct_nullPrice() {
        ProductDto productDto = new ProductDto(
                1,
                "product",
                1,
                null,
                10,
                1000
        );

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateProduct(productDto)
        );
        Assertions.assertEquals(properties.getProperty("product.price.notSpecified"), exception.getMessage());
    }

    @Test
    void ValidateProduct_negativePrice() {
        ProductDto productDto = new ProductDto(
                1,
                "product",
                1,
                -1.0,
                10,
                1000

        );

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateProduct(productDto)
        );
        Assertions.assertEquals(properties.getProperty("product.price.negative"), exception.getMessage());
    }

    @Test
    void ValidateProduct_nullDiscount() {
        ProductDto productDto = new ProductDto(
                1,
                "product",
                1,
                100.0,
                null,
                1000
        );

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateProduct(productDto)
        );
        Assertions.assertEquals(properties.getProperty("product.discount.notSpecified"), exception.getMessage());
    }

    @Test
    void ValidateProduct_negativeDiscount() {
        ProductDto productDto = new ProductDto(
                1,
                "product",
                1,
                100.0,
                -1,
                1000
        );

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateProduct(productDto)
        );
        Assertions.assertEquals(properties.getProperty("product.discount.outOfRange"), exception.getMessage());
    }

    @Test
    void ValidateProduct_discountMoreThan100() {
        ProductDto productDto = new ProductDto(
                1,
                "product",
                1,
                100.0,
                101,
                1000
        );

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateProduct(productDto)
        );
        Assertions.assertEquals(properties.getProperty("product.discount.outOfRange"), exception.getMessage());
    }

    @Test
    void ValidateProduct_nullAmount() {
        ProductDto productDto = new ProductDto(
                1,
                "product",
                1,
                100.0,
                10,
                null
        );

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateProduct(productDto)
        );
        Assertions.assertEquals(properties.getProperty("product.amount.notSpecified"), exception.getMessage());
    }

    @Test
    void ValidateProduct_negativeAmount() {
        ProductDto productDto = new ProductDto(
                1,
                "product",
                1,
                100.0,
                10,
                -1
        );

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateProduct(productDto)
        );
        Assertions.assertEquals(properties.getProperty("product.amount.negative"), exception.getMessage());
    }

    @Test
    void validateBrandId_correct() {
        Integer id = 1;

        Mockito.doReturn(true).when(brandValidatorMock).isExistById(id);

        Assertions.assertDoesNotThrow(()->productValidator.validateBrandId(id));
    }

    @Test
    void validateBrandId_nullId() {
        Integer id = null;

        Mockito.doReturn(false).when(brandValidatorMock).isExistById(id);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateBrandId(id)
        );
        Assertions.assertEquals(properties.getProperty("product.brandId.notSpecified"), exception.getMessage());
    }

    @Test
    void validateBrandId_notExistingId() {
        Integer id = 1;

        Mockito.doReturn(false).when(brandValidatorMock).isExistById(id);

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productValidator.validateBrandId(id)
        );
        Assertions.assertEquals(properties.getProperty("brand.id.notExist"), exception.getMessage());
    }

    @Test
    void validateProductTypeId_correct() {
        Integer id = 1;

        Mockito.doReturn(true).when(productTypeValidatorMock).isExistById(id);

        Assertions.assertDoesNotThrow(()->productValidator.validateProductTypeId(id));
    }

    @Test
    void validateProductTypeId_nullId() {
        Integer id = null;

        Mockito.doReturn(false).when(productTypeValidatorMock).isExistById(id);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateProductTypeId(id)
        );
        Assertions.assertEquals(properties.getProperty("product.typeId.notSpecified"), exception.getMessage());
    }

    @Test
    void validateProductTypeId_notExistingId() {
        Integer id = 1;

        Mockito.doReturn(false).when(productTypeValidatorMock).isExistById(id);

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productValidator.validateProductTypeId(id)
        );
        Assertions.assertEquals(properties.getProperty("productType.id.notExist"), exception.getMessage());
    }

    @Test
    void validateName_correct() {
        String name = "product";

        Assertions.assertDoesNotThrow(()->productValidator.validateName(name));
    }

    @Test
    void validateName_nullName() {
        String name = null;

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateName(name)
        );
        Assertions.assertEquals(properties.getProperty("product.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void validateName_blankName() {
        String name = "  ";

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateName(name)
        );
        Assertions.assertEquals(properties.getProperty("product.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void validateName_longName() {
        String name = "a".repeat(256);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateName(name)
        );
        Assertions.assertEquals(properties.getProperty("product.name.longerThan255"), exception.getMessage());
    }

    @Test
    void validatePrice_correct() {
        Double price = 100.0;

        Assertions.assertDoesNotThrow(()->productValidator.validatePrice(price));
    }

    @Test
    void validatePrice_nullPrice() {
        Double price = null;

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validatePrice(price)
        );
        Assertions.assertEquals(properties.getProperty("product.price.notSpecified"), exception.getMessage());
    }

    @Test
    void validatePrice_negativePrice() {
        Double price = -1.0;

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validatePrice(price)
        );
        Assertions.assertEquals(properties.getProperty("product.price.negative"), exception.getMessage());
    }

    @Test
    void validateDiscount_correct() {
        Integer discount = 10;

        assertDoesNotThrow(()->productValidator.validateDiscount(discount));
    }

    @Test
    void validateDiscount_nullDiscount() {
        Integer discount = null;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateDiscount(discount)
        );
        assertEquals(properties.getProperty("product.discount.notSpecified"), exception.getMessage());
    }

    @Test
    void validateDiscount_negativeDiscount() {
        Integer discount = -1;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateDiscount(discount)
        );
        assertEquals(properties.getProperty("product.discount.outOfRange"), exception.getMessage());
    }

    @Test
    void validateDiscount_discountMoreThan100() {
        Integer discount = 101;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateDiscount(discount)
        );
        assertEquals(properties.getProperty("product.discount.outOfRange"), exception.getMessage());
    }

    @Test
    void validateAmount_correct() {
        Integer amount = 100;

        assertDoesNotThrow(()->productValidator.validateAmount(amount));
    }

    @Test
    void validateAmount_nullAmount() {
        Integer amount = null;

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateAmount(amount)
        );
        Assertions.assertEquals(properties.getProperty("product.amount.notSpecified"), exception.getMessage());
    }

    @Test
    void validateAmount_negativeAmount() {
        Integer amount = -1;

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productValidator.validateAmount(amount)
        );
        Assertions.assertEquals(properties.getProperty("product.amount.negative"), exception.getMessage());
    }
}