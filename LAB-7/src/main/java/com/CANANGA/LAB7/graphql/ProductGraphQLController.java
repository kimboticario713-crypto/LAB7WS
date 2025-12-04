package com.CANANGA.LAB7.graphql;

import com.CANANGA.LAB7.entity.Product;
import com.CANANGA.LAB7.service.ProductService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductGraphQLController {

    private final ProductService productService;

    public ProductGraphQLController(ProductService productService) {
        this.productService = productService;
    }

    @QueryMapping
    public String hello() {
        return "Hello from GraphQL!";
    }

    @QueryMapping
    public List<Product> getProducts() {
        return productService.fetchAllProducts();
    }

    @QueryMapping
    public Optional<Product> getProduct(@Argument Long id) {
        return productService.fetchProductById(id);
    }

    @MutationMapping
    public Product createProduct(@Argument String name, @Argument Double price) {
        Product product = new Product(name, price);
        return productService.createProduct(product);
    }

    @MutationMapping
    public Optional<Product> updateProduct(
            @Argument Long id,
            @Argument String name,
            @Argument Double price) {

        Product productUpdate = new Product(name, price);
        return productService.modifyProduct(id, productUpdate);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        return productService.removeProduct(id);
    }
}