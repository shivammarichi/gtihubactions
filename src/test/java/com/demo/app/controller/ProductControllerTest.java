package com.demo.app.controller;

import com.demo.app.service.ProductService;
import com.demo.app.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getAllProducts_shouldReturn200WithProductList() throws Exception {
        when(productService.getAllProducts()).thenReturn(Arrays.asList(
            new Product(1L, "Laptop", "Electronics", 999.99, true)
        ));

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].name").value("Laptop"))
            .andExpect(jsonPath("$.data[0].price").value(999.99));
    }

    @Test
    void getProductById_shouldReturn200WhenFound() throws Exception {
        when(productService.getProductById(1L)).thenReturn(
            Optional.of(new Product(1L, "Laptop", "Electronics", 999.99, true))
        );

        mockMvc.perform(get("/api/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.name").value("Laptop"));
    }

    @Test
    void getProductById_shouldReturn404WhenNotFound() throws Exception {
        when(productService.getProductById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("Product not found with id: 99"));
    }
}
