package com.CANANGA.LAB7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories; // New Import

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.CANANGA.LAB7.Repository") // Added Annotation
public class Lab7Application {
    public static void main(String[] args) {
        SpringApplication.run(Lab7Application.class, args);
        System.out.println("🚀 Product Inventory API Started Successfully!");
    }
}