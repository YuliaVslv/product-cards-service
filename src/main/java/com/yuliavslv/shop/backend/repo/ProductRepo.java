package com.yuliavslv.shop.backend.repo;

import com.yuliavslv.shop.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    public List<Product> findByBrand_Id(Integer brandId);
    public List<Product> findByType_Id(Integer brandId);
}
