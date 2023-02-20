package com.yuliavslv.shop.backend.service;

import com.yuliavslv.shop.backend.BackendApplication;
import com.yuliavslv.shop.backend.dto.ProductDto;
import com.yuliavslv.shop.backend.entity.Brand;
import com.yuliavslv.shop.backend.entity.Product;
import com.yuliavslv.shop.backend.entity.ProductType;
import com.yuliavslv.shop.backend.repo.ProductRepo;
import com.yuliavslv.shop.backend.repo.ProductTypeRepo;
import com.yuliavslv.shop.backend.validator.ProductTypeValidator;
import com.yuliavslv.shop.backend.validator.ProductValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Verify ProductService class")
class ProductServiceTest {
    final static String MESSAGES_PROP = "messages_ENG.properties";

    static ProductRepo productRepoMock;
    static ProductValidator productValidatorMock;
    static BrandService brandServiceMock;
    static ProductTypeService productTypeServiceMock;

    static ProductService productService;
    static Properties properties;

    @BeforeAll
    static void init() {
        properties = new Properties();
        try(InputStream propertiesInputStream = BackendApplication.class.getClassLoader().getResourceAsStream(MESSAGES_PROP)) {
            properties.load(propertiesInputStream);
        } catch (IOException e) {
            Assertions.fail(e);
        }
        productRepoMock = Mockito.mock(ProductRepo.class);
        productValidatorMock = Mockito.mock(ProductValidator.class);
        brandServiceMock = Mockito.mock(BrandService.class);
        productTypeServiceMock = Mockito.mock(ProductTypeService.class);
        productService = new ProductService(
                productRepoMock,
                brandServiceMock,
                productTypeServiceMock,
                productValidatorMock,
                properties
        );
    }

    @Test
    void getAllProducts() {
        List<Product> products = new ArrayList<>();
        Brand brand = new Brand(1, "brand");
        ProductType productType = new ProductType(1, "category");
        Product product = new Product(brand, "product", productType, 999.99, 10, 1000);
        product.setId(1);
        products.add(product);

        Mockito.doReturn(products).when(productRepoMock).findAll();

        Assertions.assertIterableEquals(products, productService.getAll());
    }

    @Test
    void getById_found() {
        Integer id = 1;

        Brand brand = new Brand(1, "brand");
        ProductType productType = new ProductType(1, "category");
        Product product = new Product(brand, "product", productType, 999.99, 10, 1000);
        product.setId(id);

        Mockito.doReturn(Optional.of(product)).when(productRepoMock).findById(id);

        Assertions.assertEquals(product, productService.getById(id));
    }

    @Test
    void getById_notFound() {
        Integer id = 1;

        Mockito.doReturn(Optional.empty()).when(productRepoMock).findById(id);

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productService.getById(id)
        );
        Assertions.assertEquals(properties.getProperty("product.id.notExist"), exception.getMessage());
    }

    @Test
    void getAllByBrand_existing() {
        String brandName = "brand";

        Brand brand = new Brand(1, brandName);

        ProductType productType = new ProductType(1, "category");
        Product product = new Product(brand, "product", productType, 999.99, 10, 1000);
        product.setId(1);

        List<Product> products = new ArrayList<>();
        products.add(product);

        Mockito.doReturn(brand).when(brandServiceMock).getByName(brandName);
        Mockito.doReturn(products).when(productRepoMock).findByBrand_Id(brand.getId());

        Assertions.assertIterableEquals(products, productService.getAllByBrand(brandName));
    }

    @Test
    void getAllByBrand_notExisting() {
        String brandName = "brand";

        Mockito.doThrow(new NoSuchElementException(properties.getProperty("brand.name.notExist")))
                .when(brandServiceMock)
                .getByName(brandName);

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productService.getAllByBrand(brandName)
        );
        Assertions.assertEquals(
                properties.getProperty("brand.name.notExist"),
                exception.getMessage()
        );
    }

    @Test
    void getAllByProductType_existing() {
        String productTypeName = "category";

        ProductType productType = new ProductType(1, productTypeName);
        Brand brand = new Brand(1, "brand");
        Product product = new Product(brand, "product", productType, 999.99, 10, 1000);
        product.setId(1);

        List<Product> products = new ArrayList<>();
        products.add(product);

        Mockito.doReturn(productType).when(productTypeServiceMock).getByName(productTypeName);
        Mockito.doReturn(products).when(productRepoMock).findByType_Id(productType.getId());

        assertIterableEquals(products, productService.getAllByProductType(productTypeName));
    }

    @Test
    void getAllByProductType_notExisting() {
        String productTypeName = "category";

        Mockito.doThrow(new NoSuchElementException(properties.getProperty("productType.name.notExist")))
                .when(productTypeServiceMock)
                .getByName(productTypeName);

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productService.getAllByProductType(productTypeName)
        );
        Assertions.assertEquals(
                properties.getProperty("productType.name.notExist"),
                exception.getMessage()
        );
    }

    @Test
    void add_correct() {
        Integer brandId = 1;
        Integer productTypeId = 2;
        ProductDto productDto = new ProductDto(
                brandId,
                "product",
                productTypeId,
                999.99,
                10,
                1000
        );

        Brand brand = new Brand(brandId, "brand");
        ProductType productType = new ProductType(productTypeId, "category");
        Product product = new Product(
                brand,
                productDto.getName(),
                productType,
                productDto.getPrice(),
                productDto.getDiscount(),
                productDto.getAmount()
        );

        Product expected = new Product(
                1,
                brand,
                productDto.getName(),
                productType,
                productDto.getPrice(),
                productDto.getDiscount(),
                productDto.getAmount()
        );

        Mockito.doReturn(brand).when(brandServiceMock).getById(brandId);
        Mockito.doReturn(productType).when(productTypeServiceMock).getById(productTypeId);
        Mockito.doNothing().when(productValidatorMock).validateProduct(productDto);
        Mockito.doReturn(expected).when(productRepoMock).save(product);

        Assertions.assertEquals(expected, productService.add(productDto));
    }

    @Test
    void add_notExistingBrand() {
        Integer brandId = 1;
        Integer productTypeId = 2;
        ProductDto productDto = new ProductDto(
                brandId,
                "product",
                productTypeId,
                999.99,
                10,
                1000
        );

        ProductType productType = new ProductType(productTypeId, "category");

        Mockito.doThrow(new NoSuchElementException(properties.getProperty("brand.id.notExist")))
                .when(brandServiceMock)
                .getById(brandId);
        Mockito.doReturn(productType).when(productTypeServiceMock).getById(productTypeId);

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productService.add(productDto)
        );
        Assertions.assertEquals(properties.getProperty("brand.id.notExist"), exception.getMessage());
    }

    @Test
    void add_notExistingProductType() {
        Integer brandId = 1;
        Integer productTypeId = 2;
        ProductDto productDto = new ProductDto(
                brandId,
                "product",
                productTypeId,
                999.99,
                10,
                1000
        );

        Brand brand = new Brand(brandId, "brand");

        Mockito.doThrow(new NoSuchElementException(properties.getProperty("productType.id.notExist")))
                .when(productTypeServiceMock)
                .getById(productTypeId);
        Mockito.doReturn(brand).when(brandServiceMock).getById(brandId);

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productService.add(productDto)
        );
        Assertions.assertEquals(properties.getProperty("productType.id.notExist"), exception.getMessage());
    }

    @Test
    void add_incorrectData() {
        Integer brandId = 1;
        Integer productTypeId = 2;
        ProductDto productDto = new ProductDto(
                brandId,
                "product",
                productTypeId,
                -100.0,
                10,
                1000
        );

        Brand brand = new Brand(brandId, "brand");
        ProductType productType = new ProductType(productTypeId, "category");

        Mockito.doReturn(brand).when(brandServiceMock).getById(brandId);
        Mockito.doReturn(productType).when(productTypeServiceMock).getById(productTypeId);
        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("product.price.negative")))
                .when(productValidatorMock)
                .validateProduct(productDto);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productService.add(productDto)
        );
        Assertions.assertEquals(properties.getProperty("product.price.negative"), exception.getMessage());
    }

    @Test
    void delete_correct() {
        Integer id = 1;

        Brand brand = new Brand(1, "brand");
        ProductType productType = new ProductType(1, "category");
        Product product = new Product(id, brand, "product", productType, 99.99, 10, 1000);

        Mockito.doReturn(Optional.of(product)).when(productRepoMock).findById(id);
        Mockito.doNothing().when(productRepoMock).delete(product);

        Assertions.assertDoesNotThrow(()->productService.delete(id));
    }

    @Test
    void delete_notExisting() {
        Integer id = 1;

        Mockito.doReturn(Optional.empty()).when(productRepoMock).findById(id);

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productService.delete(id)
        );
        Assertions.assertEquals(properties.getProperty("product.id.notExist"), exception.getMessage());
    }

    @Test
    void change_correct() {
        Integer id = 1;
        ProductDto changes = new ProductDto(1, "product", 1, 100.0, 0, 10);
        Brand brand = new Brand(changes.getBrandId(), "brand1");
        ProductType productType = new ProductType(changes.getTypeId(), "category1");
        Product product = new Product(
                id,
                new Brand(2, "brand2"),
                "some_product",
                new ProductType(2, "category2"),
                120.0,
                10,
                5
        );
        Product expected = new Product(
                product.getId(),
                brand,
                changes.getName(),
                productType,
                changes.getPrice(),
                changes.getDiscount(),
                changes.getAmount()
        );

        Mockito.doReturn(Optional.of(product)).when(productRepoMock).findById(id);
        Mockito.doReturn(brand).when(brandServiceMock).getById(changes.getBrandId());
        Mockito.doReturn(productType).when(productTypeServiceMock).getById(changes.getTypeId());
        Mockito.doNothing().when(productValidatorMock).validateName(changes.getName());
        Mockito.doNothing().when(productValidatorMock).validatePrice(changes.getPrice());
        Mockito.doNothing().when(productValidatorMock).validateDiscount(changes.getDiscount());
        Mockito.doNothing().when(productValidatorMock).validateAmount(changes.getAmount());

        product.setBrand(brand);
        product.setName(changes.getName());
        product.setType(productType);
        product.setPrice(changes.getPrice());
        product.setAmount(changes.getAmount());
        product.setDiscount(changes.getDiscount());
        Mockito.doReturn(product).when(productRepoMock).save(product);

        Product actual = productService.change(id, changes);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void change_notExistingProduct() {
        Integer id = 1;
        ProductDto changes = new ProductDto(null, "product", null, null, null, null);

        Mockito.doReturn(Optional.empty()).when(productRepoMock).findById(id);

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productService.change(id, changes)
        );
        Assertions.assertEquals(properties.getProperty("product.id.notExist"), exception.getMessage());
    }

    @Test
    void change_notExistingBrand() {
        Integer id = 1;
        ProductDto changes = new ProductDto(2, null, null, null, null, null);
        Product product = new Product(
                id,
                new Brand(1, "brand"),
                "some_product",
                new ProductType(1, "category"),
                120.0,
                10,
                5
        );

        Mockito.doReturn(Optional.of(product)).when(productRepoMock).findById(id);
        Mockito.doThrow(new NoSuchElementException(properties.getProperty("brand.id.notExist")))
                .when(brandServiceMock)
                .getById(changes.getBrandId());

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productService.change(id, changes)
        );
        Assertions.assertEquals(properties.getProperty("brand.id.notExist"), exception.getMessage());
    }

    @Test
    void change_notExistingProductType() {
        Integer id = 1;
        ProductDto changes = new ProductDto(null, null, 3, null, null, null);
        Product product = new Product(
                id,
                new Brand(1, "brand"),
                "some_product",
                new ProductType(1, "category"),
                120.0,
                10,
                5
        );

        Mockito.doReturn(Optional.of(product)).when(productRepoMock).findById(id);
        Mockito.doThrow(new NoSuchElementException(properties.getProperty("productType.id.notExist")))
                .when(productTypeServiceMock)
                .getById(changes.getTypeId());

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productService.change(id, changes)
        );
        Assertions.assertEquals(properties.getProperty("productType.id.notExist"), exception.getMessage());
    }

    @Test
    void change_incorrectPrice() {
        Integer id = 1;
        ProductDto changes = new ProductDto(null, null, null, -10.0, null, null);
        Product product = new Product(
                id,
                new Brand(1, "brand"),
                "some_product",
                new ProductType(1, "category"),
                120.0,
                10,
                5
        );

        Mockito.doReturn(Optional.of(product)).when(productRepoMock).findById(id);
        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("product.price.negative")))
                .when(productValidatorMock)
                .validatePrice(changes.getPrice());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productService.change(id, changes)
        );
        Assertions.assertEquals(properties.getProperty("product.price.negative"), exception.getMessage());
    }

    @Test
    @DisplayName("change product to incorrect name")
    void change_incorrectName() {
        Integer id = 1;
        ProductDto changes = new ProductDto(null, "  ", null, null, null, null);
        Product product = new Product(
                id,
                new Brand(1, "brand"),
                "some_product",
                new ProductType(1, "category"),
                120.0,
                10,
                5
        );

        Mockito.doReturn(Optional.of(product)).when(productRepoMock).findById(id);
        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("product.name.nullOrEmpty")))
                .when(productValidatorMock)
                .validateName(changes.getName());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productService.change(id, changes)
        );
        Assertions.assertEquals(properties.getProperty("product.name.nullOrEmpty"), exception.getMessage());
    }

    @Test
    @DisplayName("change product to incorrect discount")
    void change_incorrectDiscount() {
        Integer id = 1;
        ProductDto changes = new ProductDto(null, null, null, null, 1000, null);
        Product product = new Product(
                id,
                new Brand(1, "brand"),
                "some_product",
                new ProductType(1, "category"),
                120.0,
                10,
                5
        );

        Mockito.doReturn(Optional.of(product)).when(productRepoMock).findById(id);
        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("product.discount.outOfRange")))
                .when(productValidatorMock)
                .validateDiscount(changes.getDiscount());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productService.change(id, changes)
        );
        Assertions.assertEquals(properties.getProperty("product.discount.outOfRange"), exception.getMessage());
    }

    @Test
    @DisplayName("change product to incorrect amount")
    void change_incorrectAmount() {
        Integer id = 1;
        ProductDto changes = new ProductDto(null, null, null, null, null, -1);
        Product product = new Product(
                id,
                new Brand(1, "brand"),
                "some_product",
                new ProductType(1, "category"),
                120.0,
                10,
                5
        );

        Mockito.doReturn(Optional.of(product)).when(productRepoMock).findById(id);
        Mockito.doThrow(new IllegalArgumentException(properties.getProperty("product.amount.negative")))
                .when(productValidatorMock)
                .validateAmount(changes.getAmount());

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->productService.change(id, changes)
        );
        Assertions.assertEquals(properties.getProperty("product.amount.negative"), exception.getMessage());
    }

    @Test
    @DisplayName("change product with no changes")
    void change_noChanges() {
        Integer id = 1;
        ProductDto changes = new ProductDto(null, null, null, null, null, null);
        Product product = new Product(
                id,
                new Brand(1, "brand"),
                "some_product",
                new ProductType(1, "category"),
                120.0,
                10,
                5
        );
        Product expected = new Product(
                id,
                new Brand(1, "brand"),
                "some_product",
                new ProductType(1, "category"),
                120.0,
                10,
                5
        );

        Mockito.doReturn(Optional.of(product)).when(productRepoMock).findById(id);
        Mockito.doReturn(product).when(productRepoMock).save(product);

        Assertions.assertEquals(expected, productService.change(id, changes));
    }

    @Test
    void getDiscounts() {
        List<Product> products =new ArrayList<>();
        Product product = new Product(
                1,
                new Brand(1, "brand"),
                "product",
                new ProductType(1, "category"),
                100.0,
                10,
                100
        );
        products.add(product);

        Mockito.doReturn(products).when(productRepoMock).findByDiscountIsGreaterThan(0);

        Assertions.assertIterableEquals(products, productService.getDiscounts());
    }

    @Test
    void setDiscount_correctBrand() {
        ProductDto changes = new ProductDto(1, null, null, null, 50, null);

        Integer expected = 1;

        Mockito.doNothing().when(productValidatorMock).validateBrandId(changes.getBrandId());
        Mockito.doThrow(IllegalArgumentException.class).when(productValidatorMock).validateProductTypeId(changes.getTypeId());
        Mockito.doNothing().when(productValidatorMock).validateDiscount(changes.getDiscount());
        Mockito.doReturn(1).when(productRepoMock).updateDiscountForBrand(changes.getBrandId(), changes.getDiscount());

        Assertions.assertEquals(expected, productService.setDiscount(changes));
    }

    @Test
    void setDiscount_correctProductType() {
        ProductDto changes = new ProductDto(null, null, 1, null, 50, null);

        Integer expected = 1;

        Mockito.doNothing().when(productValidatorMock).validateProductTypeId(changes.getTypeId());
        Mockito.doThrow(IllegalArgumentException.class).when(productValidatorMock).validateBrandId(changes.getBrandId());
        Mockito.doNothing().when(productValidatorMock).validateDiscount(changes.getDiscount());
        Mockito.doReturn(1).when(productRepoMock).updateDiscountForProductType(changes.getTypeId(), changes.getDiscount());

        Assertions.assertEquals(expected, productService.setDiscount(changes));
    }

    @Test
    void setDiscount_correctBrandAndProductType() {
        ProductDto changes = new ProductDto(1, null, 1, null, 50, null);

        Integer expected = 1;

        Mockito.doNothing().when(productValidatorMock).validateBrandId(changes.getBrandId());
        Mockito.doNothing().when(productValidatorMock).validateProductTypeId(changes.getTypeId());
        Mockito.doNothing().when(productValidatorMock).validateDiscount(changes.getDiscount());
        Mockito.doReturn(1).when(productRepoMock)
                .updateDiscountForBrandAndProductType(changes.getBrandId(), changes.getTypeId(), changes.getDiscount());

        Assertions.assertEquals(expected, productService.setDiscount(changes));
    }

    @Test
    void setDiscount_notExistingBrand() {
        ProductDto changes = new ProductDto(1, null, null, null, 50, null);

        Mockito.doThrow(new NoSuchElementException(properties.getProperty("brand.id.notExisting")))
                .when(productValidatorMock)
                .validateBrandId(changes.getBrandId());
        Mockito.doThrow(IllegalArgumentException.class).when(productValidatorMock).validateProductTypeId(changes.getTypeId());

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productService.setDiscount(changes)
        );
        Assertions.assertEquals(properties.getProperty("brand.is.notExisting"), exception.getMessage());
    }

    @Test
    void setDiscount_notExistingProductType() {
        ProductDto changes = new ProductDto(null, null, 1, null, 50, null);

        Mockito.doThrow(IllegalArgumentException.class).when(productValidatorMock).validateBrandId(changes.getBrandId());
        Mockito.doThrow(new NoSuchElementException(properties.getProperty("productType.id.notExist")))
                .when(productValidatorMock)
                .validateProductTypeId(changes.getTypeId());

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                ()->productService.setDiscount(changes)
        );
        Assertions.assertEquals(properties.getProperty("productType.id.notExist"), exception.getMessage());
    }

    @Test
    void setDiscount_incorrectDiscount() {
        ProductDto changes = new ProductDto(1, null, 1, null, -100, null);

        Mockito.doNothing().when(productValidatorMock).validateBrandId(changes.getBrandId());
        Mockito.doNothing().when(productValidatorMock).validateProductTypeId(changes.getTypeId());
        Mockito.doThrow(IllegalArgumentException.class).when(productValidatorMock).validateDiscount(changes.getDiscount());

        Assertions.assertThrows(IllegalArgumentException.class, ()->productService.setDiscount(changes));
    }

    @Test
    void setDiscount_BrandAndProductTypeNotSpecified() {
        ProductDto changes = new ProductDto(null, null, null, null, -100, null);

        Integer expected = 0;

        Mockito.doThrow(IllegalArgumentException.class).when(productValidatorMock).validateBrandId(changes.getBrandId());
        Mockito.doThrow(IllegalArgumentException.class).when(productValidatorMock).validateProductTypeId(changes.getTypeId());
        Mockito.doNothing().when(productValidatorMock).validateDiscount(changes.getDiscount());

        Assertions.assertEquals(expected, productService.setDiscount(changes));
    }
}