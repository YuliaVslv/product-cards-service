package com.yuliavslv.shop.backend.validator;


import com.yuliavslv.shop.backend.BackendApplication;
import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.ProductTypeRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class ProductTypeValidatorTest {
    final static String MESSAGES_PROP = "messages_ENG.properties";

    static ProductTypeRepo productTypeRepoMock;

    static ProductTypeValidator productTypeValidator;
    static Properties properties;


    @BeforeAll
    static void init() {
        properties = new Properties();
        try(InputStream propertiesInputStream = BackendApplication.class.getClassLoader().getResourceAsStream(MESSAGES_PROP)) {
            properties.load(propertiesInputStream);
        } catch (IOException e) {
            Assertions.fail(e);
        }

        productTypeRepoMock = Mockito.mock(ProductTypeRepo.class);
        productTypeValidator = new ProductTypeValidator(productTypeRepoMock, properties);
    }

    @Test
    void isExist_existingId() {
        Integer id = 1;
        boolean expected = true;

        Mockito.doReturn(true).when(productTypeRepoMock).existsById(id);

        Assertions.assertEquals(expected, productTypeValidator.isExistById(id));
    }

    @Test
    void isExist_notExistingId() {
        Integer id = 1;
        boolean expected = false;

        Mockito.doReturn(false).when(productTypeRepoMock).existsById(id);

        Assertions.assertEquals(expected, productTypeValidator.isExistById(id));
    }

    @Test
    void validateProductType_correct() {
        ProductType productType = new ProductType(1, "category");

        Mockito.doReturn(false).when(productTypeRepoMock).existsProductTypeByName(productType.getName());

        Assertions.assertDoesNotThrow(()->productTypeValidator.validateProductType(productType));
    }

    @Test
    void validateProductType_nullName() {
        ProductType productType = new ProductType(1, null);

        Mockito.doReturn(false).when(productTypeRepoMock).existsProductTypeByName(productType.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeValidator.validateProductType(productType)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void validateProductType_blankName() {
        ProductType productType = new ProductType(1, "  ");

        Mockito.doReturn(false).when(productTypeRepoMock).existsProductTypeByName(productType.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeValidator.validateProductType(productType)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void validateProductType_longName() {
        ProductType productType = new ProductType(1, "a".repeat(256));

        Mockito.doReturn(false).when(productTypeRepoMock).existsProductTypeByName(productType.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeValidator.validateProductType(productType)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.longerThan255"), exception.getMessage());
    }

    @Test
    void validateProductType_notUniqueName() {
        ProductType productType = new ProductType(1, "category");

        Mockito.doReturn(true).when(productTypeRepoMock).existsProductTypeByName(productType.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeValidator.validateProductType(productType)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.alreadyExist"), exception.getMessage());
    }

    @Test
    void validateName_correct() {
        String name = "category";

        Mockito.doReturn(false).when(productTypeRepoMock).existsProductTypeByName(name);

        Assertions.assertDoesNotThrow(()->productTypeValidator.validateName(name));
    }

    @Test
    void validate_nullName() {
        String name = null;

        Mockito.doReturn(false).when(productTypeRepoMock).existsProductTypeByName(name);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeValidator.validateName(name)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void validateName_blankName() {
        String name = "  ";

        Mockito.doReturn(false).when(productTypeRepoMock).existsProductTypeByName(name);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeValidator.validateName(name)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void validateName_longName() {
        String name = "a".repeat(256);

        Mockito.doReturn(false).when(productTypeRepoMock).existsProductTypeByName(name);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeValidator.validateName(name)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.longerThan255"), exception.getMessage());
    }

    @Test
    void validateName_notUniqueName() {
        String name = "category";

        Mockito.doReturn(true).when(productTypeRepoMock).existsProductTypeByName(name);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeValidator.validateName(name)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.alreadyExist"), exception.getMessage());
    }

}