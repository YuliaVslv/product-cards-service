package com.yuliavslv.shop.backend.repo;

import com.yuliavslv.shop.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {
    List<Product> findByBrand_Id(Integer brandId);
    List<Product> findByType_Id(Integer productTypeId);
    List<Product> findByDiscountIsGreaterThan(Integer lowerBound);

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.discount = :discount WHERE p.brand.id = :brandId")
    Integer updateDiscountForBrand(Integer brandId, Integer discount);

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.discount = :discount WHERE p.type.id = :productTypeId")
    Integer updateDiscountForProductType(Integer productTypeId, Integer discount);

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.discount = :discount WHERE p.brand.id = :brandId AND p.type.id = :productTypeId")
    Integer updateDiscountForBrandAndProductType(Integer brandId, Integer productTypeId, Integer discount);
}
