package com.demo.app.service;

import com.demo.app.model.Product;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    // In-memory product list — no database needed for this demo
    private final List<Product> products = Arrays.asList(
        new Product(1L, "Laptop Pro 15",    "Electronics", 1299.99, true),
        new Product(2L, "Mechanical Keyboard","Electronics",  149.99, true),
        new Product(3L, "USB-C Hub",         "Accessories",   49.99, true),
        new Product(4L, "Monitor 27-inch",   "Electronics",  399.99, false),
        new Product(5L, "Desk Chair",        "Furniture",    299.99, true)
    );

    /**
     * Returns all products.
     */
    public List<Product> getAllProducts() {
        return products;
    }

    /**
     * Returns a single product by ID.
     * Returns Optional.empty() if not found.
     */
    public Optional<Product> getProductById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }
}
