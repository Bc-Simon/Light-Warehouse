package com.simon;

import com.simon.model.Product;
import com.simon.repository.ProductRepository;
import org.json.JSONObject;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductTest {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private HttpHeaders httpHeaders = new HttpHeaders();

    @After
    public void clear() {
        productRepository.deleteAll();
    }

    @Test
    public void testCreateProduct() throws Exception {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        JSONObject request = new JSONObject();
        request.put("code", "FM-HKTV01");
        request.put("name", "face mask1");
        request.put("weight", 100);

        RequestBuilder requestBuilder =
                MockMvcRequestBuilders
                        .post("/api/products")
                        .headers(httpHeaders)
                        .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(request.getString("code")))
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.weight").value(request.getInt("weight")));
    }

    @Test
    public void testGetProduct() throws Exception {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Product product = createProduct("FM-HKTV02", "face mask2", 200);
        productRepository.save(product);

        mockMvc.perform(get("/api/products/" + product.getCode())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(product.getCode()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.weight").value(product.getWeight()));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Product product = createProduct("FM-HKTV03", "face mask3", 300);
        productRepository.save(product);

        JSONObject request = new JSONObject();
        request.put("name", "3mask");

        mockMvc.perform(put("/api/products/" + product.getCode())
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(product.getCode()))
                .andExpect(jsonPath("$.name").value(request.getString("name")));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product product = createProduct("FM-HKTV04", "face mask4", 400);
        productRepository.save(product);

        mockMvc.perform(delete("/api/products/" + product.getCode())
                .headers(httpHeaders))
                .andExpect(status().isNoContent());

    }


    private Product createProduct(String code, String name, long weight) {
        return new Product(code, name, weight);
    }
}
