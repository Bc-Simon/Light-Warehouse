package com.simon.csv_handler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

import com.simon.model.Stock;
import org.springframework.web.multipart.MultipartFile;

public class CSVStockHelper {
  //Used in Controller to Check incoming file
  public static boolean hasCSVFormat(MultipartFile file) {
    return true;
  }
  static String[] STOCK_HEADER = { "location", "code", "quantity" };
  public static List<Stock> csvToStocks(InputStream is) {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
         CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

      List<Stock> pendingStocks = new ArrayList<>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();

      for (CSVRecord csvRecord : csvRecords) {     	
        Stock stock = new Stock(
        	  csvRecord.get("location"),
        	  csvRecord.get("code"),
              Long.parseLong(csvRecord.get("quantity"))
            );

        pendingStocks.add(stock);
      }

      return pendingStocks;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }

  public static ByteArrayInputStream stocksToCSV(List<Stock> stocks) {
    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
      
      csvPrinter.printRecord(STOCK_HEADER);

      for (Stock stock : stocks) {
        List<String> data = Arrays.asList(
              stock.getLocation(),
              stock.getCode(),
              String.valueOf(stock.getQuantity())
            );

        csvPrinter.printRecord(data);
      }

      csvPrinter.flush();
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
    }
  }

}
