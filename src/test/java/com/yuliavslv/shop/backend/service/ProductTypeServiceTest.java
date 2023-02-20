package com.yuliavslv.shop.backend.service;

import com.yuliavslv.shop.backend.BackendApplication;
import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.ProductTypeRepo;
import com.yuliavslv.shop.backend.validator.ProductTypeValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@DisplayName("Verify ProductTypeService class")
class ProductTypeServiceTest {
    final static String MESSAGES_PROP = "messages_ENG.properties";

    static ProductTypeRepo productTypeRepoMock;
    static ProductTypeValidator productTypeValidatorMock;

    static ProductTypeService productTypeService;
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
        productTypeValidatorMock = Mockito.mock(ProductTypeValidator.class);
        productTypeService = new ProductTypeService(productTypeRepoMock, productTypeValidatorMock, properties);
    }

    @Test
    void getAll() {
        List<ProductType> expected = new ArrayList<>();
        ProductType productType1 = new ProductType(1, "category1");
        ProductType productType2 = new ProductType(2, "category2");
        expected.add(productType1);
        expected.add(productType2);

        Mockito.doReturn(expected).when(productTypeRepoMock).findAll();

        Assertions.assertIterableEquals(expected, productTypeService.getAll());
    }

    @Test
    void getById_found() {
        Integer id = 1;
        ProductType expected = new ProductType(id, "category");

        Mockito.doReturn(Optional.of(expected)).when(productTypeRepoMock).findById(id);

        Assertions.assertEquals(expected, productTypeService.getById(id));
    }

    @Test
    void getById_notFound() {
        Integer id = 1;

        Mockito.doReturn(Optional.empty()).when(productTypeRepoMock).findById(id);
        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> productTypeService.getById(id)
        );

        Assertions.assertEquals(properties.getProperty("productType.id.notExist"), exception.getMessage());
    }

    @Test
    void getByName_found() {
        String name = "category";
        ProductType expected = new ProductType(1, name);

        Mockito.doReturn(Optional.of(expected)).when(productTypeRepoMock).findProductTypeByName(name);

        Assertions.assertEquals(expected, productTypeService.getByName(name));
    }

    @Test
    void getByName_notFound() {
        String name ="category";
        Mockito.doReturn(Optional.empty()).when(productTypeRepoMock).findProductTypeByName(name);

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> productTypeService.getByName(name)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.notExist"), exception.getMessage());
    }

    @Test
    void add_correct() {
        ProductType productType = new ProductType(null, "category");

        ProductType expected = new ProductType(1, productType.getName());

        Mockito.doNothing().when(productTypeValidatorMock).validateProductType(productType);
        Mockito.doReturn(expected).when(productTypeRepoMock).save(productType);

        Assertions.assertEquals(expected, productTypeService.add(productType));
    }

    @Test
    void add_notUniqueName() {
        ProductType productType = new ProductType(null, "category");

        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("productType.name.alreadyExist")))
                .when(productTypeValidatorMock).validateProductType(productType);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeService.add(productType)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.alreadyExist"), exception.getMessage());
    }

    @Test
    void add_blankName() {
        ProductType productType = new ProductType(null, "");

        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("productType.name.nullOrEmpty")))
                .when(productTypeValidatorMock).validateProductType(productType);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeService.add(productType)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void add_longName() {
        ProductType productType = new ProductType(null, "a".repeat(256));

        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("productType.name.longerThan255")))
                .when(productTypeValidatorMock).validateProductType(productType);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeService.add(productType)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.longerThan255"), exception.getMessage());
    }

    @Test
    void add_nullName() {
        ProductType productType = new ProductType(null, null);

        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("productType.name.nullOrEmpty")))
                .when(productTypeValidatorMock).validateProductType(productType);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeService.add(productType)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void delete_correct() {
        String name = "category";
        ProductType productType = new ProductType(1, name);

        Mockito.doReturn(Optional.of(productType)).when(productTypeRepoMock).findProductTypeByName(name);
        Mockito.doNothing().when(productTypeRepoMock).delete(productType);

        Assertions.assertDoesNotThrow(()->productTypeService.delete(name));
    }

    @Test
    void delete_associatedProductType() {
        String name = "category";
        ProductType productType = new ProductType(1, name);

        Mockito.doReturn(Optional.of(productType)).when(productTypeRepoMock).findProductTypeByName(name);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productTypeRepoMock).delete(productType);

        Assertions.assertThrows(DataIntegrityViolationException.class, ()->productTypeService.delete(name));
    }

    @Test
    void delete_notExistingProductType() {
        String name = "category";
        Mockito.doReturn(Optional.empty()).when(productTypeRepoMock).findProductTypeByName(name);

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productTypeService.delete(name)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.notExist"), exception.getMessage());
    }

    @Test
    void change_correct() {
        ProductType changes = new ProductType(null, "category2");

        String name = "category1";
        ProductType productType = new ProductType(1, name);

        Mockito.doReturn(Optional.of(productType)).when(productTypeRepoMock).findProductTypeByName(name);
        Mockito.doNothing().when(productTypeValidatorMock).validateName(changes.getName());
        Mockito.doNothing().when(productTypeRepoMock).updateProductTypeName(productType.getId(), changes.getName());

        ProductType expected = new ProductType();
        expected.setId(productType.getId());
        expected.setName(changes.getName());

        Assertions.assertEquals(expected, productTypeService.change(name, changes));
    }

    @Test
    void change_notUniqueName() {
        ProductType changes = new ProductType(null, "category2");

        String name = "category1";
        ProductType productType = new ProductType(1, name);

        Mockito.doReturn(Optional.of(productType)).when(productTypeRepoMock).findProductTypeByName(name);
        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("productType.name.alreadyExist")))
                .when(productTypeValidatorMock).validateName(changes.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeService.change(name, changes)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.alreadyExist"), exception.getMessage());
    }

    @Test
    void change_blankName() {
        ProductType changes = new ProductType(null, "");

        String name = "category1";
        ProductType productType = new ProductType(1, name);

        Mockito.doReturn(Optional.of(productType)).when(productTypeRepoMock).findProductTypeByName(name);
        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("productType.name.nullOrEmpty")))
                .when(productTypeValidatorMock).validateName(changes.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeService.change(name, changes)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void change_nullName() {
        ProductType changes = new ProductType(null, null);

        String name = "category1";
        ProductType productType = new ProductType(1, name);

        Mockito.doReturn(Optional.of(productType)).when(productTypeRepoMock).findProductTypeByName(name);

        Assertions.assertEquals(productType, productTypeService.change(name, changes));
    }

    @Test
    void change_longName() {
        ProductType changes = new ProductType(null, "a".repeat(256));

        String name = "category1";
        ProductType productType = new ProductType(1, name);

        Mockito.doReturn(Optional.of(productType)).when(productTypeRepoMock).findProductTypeByName(name);
        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("productType.name.longerThan255")))
                .when(productTypeValidatorMock).validateName(changes.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productTypeService.change(name, changes)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.longerThan255"), exception.getMessage());
    }

    @Test
    void change_notExistingProductType() {
        ProductType changes = new ProductType(null, "category2");

        String name = "category1";

        Mockito.doReturn(Optional.empty()).when(productTypeRepoMock).findProductTypeByName(name);
        Mockito.doNothing().when(productTypeValidatorMock).validateName(changes.getName());

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productTypeService.change(name, changes)
        );
        Assertions.assertEquals(properties.getProperty("productType.name.notExist"), exception.getMessage());
    }
}