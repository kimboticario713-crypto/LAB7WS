package com.CANANGA.LAB7.service;

import org.springframework.stereotype.Service;
import com.CANANGA.LAB7.entity.Product;
import com.CANANGA.LAB7.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        initializeSampleProducts();
    }

    private void initializeSampleProducts() {
        if (productRepository.count() == 0) {
            saveProduct(new Product("Quantum Laptop X1", 1299.99));
            saveProduct(new Product("Neural Keyboard Pro", 149.50));
            saveProduct(new Product("Holographic Monitor", 899.00));
        }
    }

    private void saveProduct(Product product) {
        productRepository.save(product);
    }

    public List<Product> fetchAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> fetchProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Optional<Product> modifyProduct(Long id, Product productUpdate) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(productUpdate.getName());
            existingProduct.setPrice(productUpdate.getPrice());
            return productRepository.save(existingProduct);
        });
    }

    @Transactional
    public boolean removeProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean productExists(Long id) {
        return productRepository.existsById(id);
    }
}