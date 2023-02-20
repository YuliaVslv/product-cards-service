package com.yuliavslv.shop.backend.service;

import com.yuliavslv.shop.backend.BackendApplication;
import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import com.yuliavslv.shop.backend.validator.BrandValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@DisplayName("Verify BrandService class")
class BrandServiceTest {
    final static String MESSAGES_PROP = "messages_ENG.properties";

    static BrandRepo brandRepoMock;
    static BrandValidator brandValidatorMock;

    static BrandService brandService;
    static Properties properties;

    @BeforeAll
    static void init() {
        properties = new Properties();
        try(InputStream propertiesInputStream = BackendApplication.class.getClassLoader().getResourceAsStream(MESSAGES_PROP)) {
            properties.load(propertiesInputStream);
        } catch (IOException e) {
            Assertions.fail(e);
        }
        brandRepoMock = Mockito.mock(BrandRepo.class);
        brandValidatorMock = Mockito.mock(BrandValidator.class);
        brandService = new BrandService(brandRepoMock, brandValidatorMock, properties);
    }

    @Test
    void getAllBrands() {
        List<Brand> expected = new ArrayList<>();
        Brand brand1 = new Brand(1, "brand1");
        Brand brand2 = new Brand(2, "brand2");
        expected.add(brand1);
        expected.add(brand2);

        Mockito.doReturn(expected).when(brandRepoMock).findAll();

        Assertions.assertIterableEquals(expected, brandService.getAll());
    }

    @Test
    void getById_found() {
        Integer id = 1;
        Brand expected = new Brand(id, "brand");

        Mockito.doReturn(Optional.of(expected)).when(brandRepoMock).findById(id);

        Assertions.assertEquals(expected, brandService.getById(id));
    }

    @Test
    void getById_notFound() {
        Integer id = 1;

        Mockito.doReturn(Optional.empty()).when(brandRepoMock).findById(id);

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> brandService.getById(id)
        );
        Assertions.assertEquals(properties.getProperty("brand.id.notExist"), exception.getMessage());
    }

    @Test
    void getByName_found() {
        String name = "brand";
        Brand expected = new Brand(1, name);

        Mockito.doReturn(Optional.of(expected)).when(brandRepoMock).findBrandByName(name);

        Assertions.assertEquals(expected, brandService.getByName(name));
    }

    @Test
    void getByName_notFound() {
        String name ="brand";

        Mockito.doReturn(Optional.empty()).when(brandRepoMock).findBrandByName(name);

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> brandService.getByName(name)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.notExist"), exception.getMessage());
    }

    @Test
    @DisplayName("add the correct brand")
    void add_correct() {
        Brand brand = new Brand(null, "brand");

        Brand expected = new Brand(1, brand.getName());

        Mockito.doNothing().when(brandValidatorMock).validateBrand(brand);
        Mockito.doReturn(expected).when(brandRepoMock).save(brand);
        
        Assertions.assertEquals(expected, brandService.add(brand));
    }

    @Test
    void add_notUniqueName() {
        Brand brand = new Brand(null, "brand");

        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("brand.name.alreadyExist")))
                .when(brandValidatorMock).validateBrand(brand);
        
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandService.add(brand)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.alreadyExist"), exception.getMessage());
    }

    @Test
    void add_blankName() {
        Brand brand = new Brand(null, "");

        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("brand.name.nullOrEmpty")))
                .when(brandValidatorMock).validateBrand(brand);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandService.add(brand)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void add_longName() {
        Brand brand = new Brand(null, "a".repeat(256));

        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("brand.name.longerThan255")))
                .when(brandValidatorMock).validateBrand(brand);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandService.add(brand)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.longerThan255"), exception.getMessage());
    }

    @Test
    void add_nullName() {
        Brand brand = new Brand(null, null);

        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("brand.name.nullOrEmpty")))
                .when(brandValidatorMock).validateBrand(brand);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandService.add(brand)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void delete_correct() {
        String name = "brand";
        Brand brand = new Brand(1, name);

        Mockito.doReturn(Optional.of(brand)).when(brandRepoMock).findBrandByName(name);
        Mockito.doNothing().when(brandRepoMock).delete(brand);

        Assertions.assertDoesNotThrow(()->brandService.delete(name));
    }

    @Test
    void delete_associatedBrand() {
        String name = "brand";
        Brand brand = new Brand(1, name);

        Mockito.doReturn(Optional.of(brand)).when(brandRepoMock).findBrandByName(name);
        Mockito.doThrow(DataIntegrityViolationException.class).when(brandRepoMock).delete(brand);

        Assertions.assertThrows(DataIntegrityViolationException.class, ()->brandService.delete(name));
    }

    @Test
    void delete_notExistingBrand() {
        String name = "brand";
        Mockito.doReturn(Optional.empty()).when(brandRepoMock).findBrandByName(name);

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->brandService.delete(name)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.notExist"), exception.getMessage());
    }

    @Test
    void change_correct() {
        Brand changes = new Brand(null, "brand2");

        String name = "brand1";
        Brand brand = new Brand(1, name);

        Mockito.doReturn(Optional.of(brand)).when(brandRepoMock).findBrandByName(name);
        Mockito.doNothing().when(brandValidatorMock).validateName(changes.getName());
        Mockito.doNothing().when(brandRepoMock).updateBrandName(brand.getId(), changes.getName());

        Brand expected = new Brand();
        expected.setId(brand.getId());
        expected.setName(changes.getName());

        Assertions.assertEquals(expected, brandService.change(name, changes));
    }

    @Test
    void change_notUniqueName() {
        Brand changes = new Brand(null, "brand2");

        String name = "brand1";
        Brand brand = new Brand(1, name);

        Mockito.doReturn(Optional.of(brand)).when(brandRepoMock).findBrandByName(name);
        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("brand.name.alreadyExist")))
                .when(brandValidatorMock).validateName(changes.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandService.change(name, changes)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.alreadyExist"), exception.getMessage());
    }

    @Test
    void change_blankName() {
        Brand changes = new Brand(null, "");

        String name = "brand1";
        Brand brand = new Brand(1, name);

        Mockito.doReturn(Optional.of(brand)).when(brandRepoMock).findBrandByName(name);
        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("brand.name.nullOrEmpty")))
                .when(brandValidatorMock).validateName(changes.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandService.change(name, changes)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void change_nullName() {
        Brand changes = new Brand(null, null);

        String name = "brand1";
        Brand brand = new Brand(1, name);

        Mockito.doReturn(Optional.of(brand)).when(brandRepoMock).findBrandByName(name);

        Assertions.assertEquals(brand, brandService.change(name, changes));
    }

    @Test
    void change_longName() {
        Brand changes = new Brand(null, "a".repeat(256));

        String name = "brand1";
        Brand brand = new Brand(1, name);

        Mockito.doReturn(Optional.of(brand)).when(brandRepoMock).findBrandByName(name);
        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("brand.name.longerThan255")))
                .when(brandValidatorMock).validateName(changes.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandService.change(name, changes)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.longerThan255"), exception.getMessage());
    }

    @Test
    void change_notExistingBrand() {
        Brand changes = new Brand(null, "brand2");

        String name = "brand1";

        Mockito.doReturn(Optional.empty()).when(brandRepoMock).findBrandByName(name);
        Mockito.doNothing().when(brandValidatorMock).validateName(changes.getName());

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->brandService.change(name, changes)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.notExist"), exception.getMessage());
    }
}