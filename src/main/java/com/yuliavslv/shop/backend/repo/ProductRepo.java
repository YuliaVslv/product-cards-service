package com.yuliavslv.shop.backend.repo;

import com.yuliavslv.shop.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Integer> {
}
