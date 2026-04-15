package com.demo.app.controller;

import com.demo.app.model.ApiResponse;
import com.demo.app.model.Product;
import com.demo.app.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // Constructor injection (best practice over @Autowired)
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * GET /api/products
     * Returns all products.
     *
     * Example: curl http://localhost:8080/api/products
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Products fetched successfully", products)
        );
    }

    /**
     * GET /api/products/{id}
     * Returns a single product by its ID.
     * Returns 404 if the product is not found.
     *
     * Example: curl http://localhost:8080/api/products/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);

        if (product.isEmpty()) {
            return ResponseEntity.status(404).body(
                new ApiResponse<>(false, "Product not found with id: " + id, null)
            );
        }

        return ResponseEntity.ok(
            new ApiResponse<>(true, "Product fetched successfully", product.get())
        );
    }
}
