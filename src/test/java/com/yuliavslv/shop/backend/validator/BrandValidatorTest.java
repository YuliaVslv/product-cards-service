package com.yuliavslv.shop.backend.validator;

import com.yuliavslv.shop.backend.BackendApplication;
import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.repo.BrandRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@DisplayName("Verify BrandValidator class")
class BrandValidatorTest {
    final static String MESSAGES_PROP = "messages_ENG.properties";

    static BrandRepo brandRepoMock;

    static BrandValidator brandValidator;
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
        brandValidator = new BrandValidator(brandRepoMock, properties);
    }

    @Test
    void isExist_existingId() {
        Integer id = 1;
        boolean expected = true;

        Mockito.doReturn(true).when(brandRepoMock).existsById(id);

        Assertions.assertEquals(expected, brandValidator.isExistById(id));
    }

    @Test
    void isExist_notExistingId() {
        Integer id = 1;
        boolean expected = false;

        Mockito.doReturn(false).when(brandRepoMock).existsById(id);

        Assertions.assertEquals(expected, brandValidator.isExistById(id));
    }

    @Test
    void validateBrand_correct() {
        Brand brand = new Brand(1, "brand");

        Mockito.doReturn(false).when(brandRepoMock).existsBrandByName(brand.getName());

        Assertions.assertDoesNotThrow(()->brandValidator.validateBrand(brand));
    }

    @Test
    void validateBrand_nullName() {
        Brand brand = new Brand(1, null);

        Mockito.doReturn(false).when(brandRepoMock).existsBrandByName(brand.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandValidator.validateBrand(brand)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void validateBrand_blankName() {
        Brand brand = new Brand(1, "  ");

        Mockito.doReturn(false).when(brandRepoMock).existsBrandByName(brand.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandValidator.validateBrand(brand)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    @DisplayName("validate brand with long name")
    void validateBrand_longName() {
        Brand brand = new Brand(1, "a".repeat(256));

        Mockito.doReturn(false).when(brandRepoMock).existsBrandByName(brand.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandValidator.validateBrand(brand)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.longerThan255"), exception.getMessage());
    }

    @Test
    void validateBrand_notUniqueName() {
        Brand brand = new Brand(1, "brand");

        Mockito.doReturn(true).when(brandRepoMock).existsBrandByName(brand.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandValidator.validateBrand(brand)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.alreadyExist"), exception.getMessage());
    }

    @Test
    void validateName_correct() {
        String name = "brand";

        Mockito.doReturn(false).when(brandRepoMock).existsBrandByName(name);

        Assertions.assertDoesNotThrow(()->brandValidator.validateName(name));
    }

    @Test
    void validateName_nullName() {
        String name = null;

        Mockito.doReturn(false).when(brandRepoMock).existsBrandByName(name);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandValidator.validateName(name)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void validateName_blankName() {
        String name = "  ";

        Mockito.doReturn(false).when(brandRepoMock).existsBrandByName(name);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandValidator.validateName(name)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    void validateName_longName() {
        String name = "a".repeat(256);

        Mockito.doReturn(false).when(brandRepoMock).existsBrandByName(name);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandValidator.validateName(name)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.longerThan255"), exception.getMessage());
    }

    @Test
    void validateName_notUniqueName() {
        String name = "brand";

        Mockito.doReturn(true).when(brandRepoMock).existsBrandByName(name);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->brandValidator.validateName(name)
        );
        Assertions.assertEquals(properties.getProperty("brand.name.alreadyExist"), exception.getMessage());
    }
}