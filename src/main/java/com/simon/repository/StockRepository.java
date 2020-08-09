package com.simon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.simon.model.Stock;
import com.simon.model.Sku;

public interface StockRepository extends JpaRepository<Stock, Sku> {
}
