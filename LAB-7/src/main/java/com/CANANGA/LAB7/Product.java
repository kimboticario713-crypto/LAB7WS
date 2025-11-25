package com.CANANGA.LAB7;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private Double price;

    // Custom constructor without ID for creation
    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }
}