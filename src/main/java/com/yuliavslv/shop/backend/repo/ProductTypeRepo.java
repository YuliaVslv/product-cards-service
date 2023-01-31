package com.yuliavslv.shop.backend.repo;

import com.yuliavslv.shop.backend.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductTypeRepo extends JpaRepository<ProductType, Integer> {
    Optional<ProductType> findProductTypeByName(String name);
    boolean existsProductTypeByName(String name);
}
