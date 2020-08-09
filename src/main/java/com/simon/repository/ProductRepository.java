package com.simon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.simon.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
}
