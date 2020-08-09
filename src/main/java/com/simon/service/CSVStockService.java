package com.simon.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.simon.csv_handler.CSVStockHelper;
import com.simon.model.Stock;
import com.simon.repository.StockRepository;

@Service
public class CSVStockService {
  @Autowired
  StockRepository repository;

  public void importStocks(MultipartFile file) {
    try {
      List<Stock> stocks = CSVStockHelper.csvToStocks(file.getInputStream());
      repository.saveAll(stocks);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }

  public ByteArrayInputStream exportStocks() {
    List<Stock> stocks = repository.findAll();

    return CSVStockHelper.stocksToCSV(stocks);
  }

}
