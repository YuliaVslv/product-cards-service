package com.yuliavslv.shop.backend.repo;

import com.yuliavslv.shop.backend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepo extends JpaRepository<Brand, Integer> {
    Optional<Brand> findBrandByName(String name);
}
