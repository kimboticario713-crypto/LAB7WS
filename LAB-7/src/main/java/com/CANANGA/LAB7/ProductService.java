package com.CANANGA.LAB7.service;

import org.springframework.stereotype.Service;
import com.CANANGA.LAB7.Product;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {
    private final Map<Long, Product> productDatabase = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1000); // Starting from 1000 for uniqueness

    // Initialize with mock data
    public ProductService() {
        initializeSampleProducts();
    }

    private void initializeSampleProducts() {
        saveProduct(new Product("Quantum Laptop X1", 1299.99));
        saveProduct(new Product("Neural Keyboard Pro", 149.50));
        saveProduct(new Product("Holographic Monitor", 899.00));
    }

    private void saveProduct(Product product) {
        Long newId = idGenerator.incrementAndGet();
        product.setId(newId);
        productDatabase.put(newId, product);
    }

    // Get all products
    public List<Product> fetchAllProducts() {
        return new ArrayList<>(productDatabase.values());
    }

    // Get product by ID
    public Optional<Product> fetchProductById(Long id) {
        return Optional.ofNullable(productDatabase.get(id));
    }

    // Create new product
    public Product createProduct(Product product) {
        Long newId = idGenerator.incrementAndGet();
        product.setId(newId);
        productDatabase.put(newId, product);
        return product;
    }

    // Update existing product
    public Optional<Product> modifyProduct(Long id, Product productUpdate) {
        return fetchProductById(id).map(existingProduct -> {
            existingProduct.setName(productUpdate.getName());
            existingProduct.setPrice(productUpdate.getPrice());
            productDatabase.put(id, existingProduct);
            return existingProduct;
        });
    }

    // Delete product
    public boolean removeProduct(Long id) {
        return productDatabase.remove(id) != null;
    }

    // Check if product exists
    public boolean productExists(Long id) {
        return productDatabase.containsKey(id);
    }
}