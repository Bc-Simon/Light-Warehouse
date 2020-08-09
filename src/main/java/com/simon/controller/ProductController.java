package com.simon.controller;

import com.simon.csv_handler.CSVProductHelper;
import com.simon.model.Product;
import com.simon.repository.ProductRepository;
import com.simon.response_message.ResponseMessage;
import com.simon.service.CSVProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {
  
  @Autowired
  ProductRepository productRepository;

  @Autowired
  CSVProductService CSVService;

  @GetMapping("/products")
  public ResponseEntity<List<Product>> getAllProducts() {
    try {

      List<Product> products = new ArrayList<>(productRepository.findAll());

      if (products.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(products, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/products")
  public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    try {
      Product _product = productRepository
          .save(new Product(product.getCode(), product.getName(), product.getWeight()));
      return new ResponseEntity<>(_product, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PutMapping("/products/{code}")
  public ResponseEntity<Product> updateProduct(@PathVariable("code") String code, @RequestBody Product product) {
    Optional<Product> productData = productRepository.findById(code);

    if (productData.isPresent()) {
      Product _product = productData.get();
      _product.setName(product.getName());
      _product.setWeight(product.getWeight());
      return new ResponseEntity<>(productRepository.save(_product), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/products/{code}")
  public ResponseEntity<Product> getProductByCode(@PathVariable("code") String code) {
    Optional<Product> productData = productRepository.findById(code);

    return productData.map(product -> new ResponseEntity<>(product, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/products/{code}")
  public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("code") String code) {
    try {
      productRepository.deleteById(code);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PostMapping("/products/upload_csv")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message;

    if (CSVProductHelper.hasCSVFormat(file)) {
      try {
        CSVService.importProducts(file);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      } catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      }
    }

    message = "Please upload a csv file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
  }

  @GetMapping("/products/download_csv")
  public ResponseEntity<Resource> getFile() {
    String filename = "products.csv";
    InputStreamResource file = new InputStreamResource(CSVService.exportProducts());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(file);
  }
}
