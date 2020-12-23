package com.smarterwith.catalogservice.repository;

import com.smarterwith.catalogservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>{
    
}
