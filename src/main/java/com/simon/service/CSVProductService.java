package com.simon.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.simon.csv_handler.CSVProductHelper;
import com.simon.model.Product;
import com.simon.repository.ProductRepository;

@Service
public class CSVProductService {
  @Autowired
  ProductRepository repository;

  public void importProducts(MultipartFile file) {
    try {
      List<Product> products = CSVProductHelper.csvToProducts(file.getInputStream());
      repository.saveAll(products);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }

  public ByteArrayInputStream exportProducts() {
    List<Product> products = repository.findAll();

    return CSVProductHelper.productsToCSV(products);
  }

}
