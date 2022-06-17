package com.works.repositories;

import com.works.entities.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    List<Basket> findByCreatedByEqualsAndStatusFalse(String createdBy);

    List<Basket> findByCreatedByEqualsIgnoreCase(String createdBy);


}