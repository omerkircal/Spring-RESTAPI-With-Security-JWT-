package com.works.repositories;

import com.works.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductNameContainsIgnoreCase(String productName);

    List<Product> findByCategory_IdEquals(Long id);


}