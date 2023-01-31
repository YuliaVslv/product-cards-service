package com.yuliavslv.shop.backend.repo;

import com.yuliavslv.shop.backend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BrandRepo extends JpaRepository<Brand, Integer> {
    Optional<Brand> findBrandByName(String name);
    boolean existsBrandByName(String name);

    @Transactional
    @Modifying
    @Query("UPDATE Brand b SET b.name = :newName WHERE b.id = :id")
    void updateBrandName(@Param(value = "id") Integer id, @Param(value = "newName") String newName);
}
