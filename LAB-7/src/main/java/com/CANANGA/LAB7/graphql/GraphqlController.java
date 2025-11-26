package com.CANANGA.LAB7.graphql;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/graphql")
public class GraphqlController {

    @PostMapping
    public Map<String, Object> executeQuery(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String query = (String) request.get("query");
            Map<String, Object> variables = (Map<String, Object>) request.get("variables");

            if (query == null || query.trim().isEmpty()) {
                response.put("data", null);
                response.put("errors", "Query cannot be empty");
                return response;
            }

            // Simple query processing
            if (query.contains("hello")) {
                response.put("data", Map.of("hello", "Hello from GraphQL Controller!"));
            }
            else if (query.contains("getUsers")) {
                response.put("data", Map.of("getUsers", getSampleUsers()));
            }
            else if (query.contains("getProducts")) {
                response.put("data", Map.of("getProducts", getSampleProducts()));
            }
            else {
                response.put("data", null);
                response.put("errors", "Unknown query: " + query);
            }

        } catch (Exception e) {
            response.put("data", null);
            response.put("errors", e.getMessage());
        }

        return response;
    }

    @GetMapping("/test")
    public String test() {
        return "GraphQL Controller is working! Use POST /api/graphql for queries.";
    }

    @GetMapping("/schema")
    public String getSchema() {
        return """
            Available Queries:
            - hello: String
            - getUsers: [User]
            - getProducts: [Product]
            
            Example query: 
            { "query": "query { hello getUsers { id name } }" }
            """;
    }

    private Map<String, Object>[] getSampleUsers() {
        return new Map[] {
                Map.of("id", "1", "name", "Kim Cananga", "email", "cananga@gmail.com"),
                Map.of("id", "2", "name", "Edison Damayo", "email", "damayo@gmail.com.com"),
                Map.of("id", "3", "name", "Shierafel Boticario", "email", "boticario@gmail.com.com")
        };
    }

    private Map<String, Object>[] getSampleProducts() {
        return new Map[] {
                Map.of("id", "101", "name", "Laptop", "price", 999.99, "category", "Electronics"),
                Map.of("id", "102", "name", "Mouse", "price", 29.99, "category", "Electronics"),
                Map.of("id", "103", "name", "Notebook", "price", 5.99, "category", "Stationery")
        };
    }
}