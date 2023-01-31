package com.yuliavslv.shop.backend.repo;

import com.yuliavslv.shop.backend.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProductTypeRepo extends JpaRepository<ProductType, Integer> {
    Optional<ProductType> findProductTypeByName(String name);
    boolean existsProductTypeByName(String name);

    @Transactional
    @Modifying
    @Query("UPDATE ProductType pt SET pt.name = :newName WHERE pt.id = :id")
    void updateProductTypeName(@Param(value = "id") Integer id, @Param(value = "newName") String newName);
}
