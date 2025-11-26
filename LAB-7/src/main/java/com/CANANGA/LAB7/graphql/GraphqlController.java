package com.CANANGA.LAB7.graphql;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/graphql")
public class GraphqlController {

    // In-memory storage (replace with database in production)
    private final Map<String, Map<String, Object>> users = new HashMap<>();
    private final Map<String, Map<String, Object>> products = new HashMap<>();
    private final AtomicLong userIdCounter = new AtomicLong(3); // Starting after sample data
    private final AtomicLong productIdCounter = new AtomicLong(103); // Starting after sample data

    public GraphqlController() {
        // Initialize with sample data
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Add sample users
        Map<String, Object> user1 = Map.of("id", "1", "name", "Kim Cananga", "email", "cananga@gmail.com");
        Map<String, Object> user2 = Map.of("id", "2", "name", "Edison Damayo", "email", "damayo@gmail.com");
        Map<String, Object> user3 = Map.of("id", "3", "name", "Shierafel Boticario", "email", "boticario@gmail.com");

        users.put("1", user1);
        users.put("2", user2);
        users.put("3", user3);

        // Add sample products
        Map<String, Object> product1 = Map.of("id", "101", "name", "Laptop", "price", 999.99, "category", "Electronics");
        Map<String, Object> product2 = Map.of("id", "102", "name", "Mouse", "price", 29.99, "category", "Electronics");
        Map<String, Object> product3 = Map.of("id", "103", "name", "Notebook", "price", 5.99, "category", "Stationery");

        products.put("101", product1);
        products.put("102", product2);
        products.put("103", product3);
    }

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

            // Process queries
            if (query.contains("query")) {
                processQuery(query, variables, response);
            }
            // Process mutations
            else if (query.contains("mutation")) {
                processMutation(query, variables, response);
            }
            else {
                response.put("data", null);
                response.put("errors", "Unknown operation type");
            }

        } catch (Exception e) {
            response.put("data", null);
            response.put("errors", e.getMessage());
        }

        return response;
    }

    private void processQuery(String query, Map<String, Object> variables, Map<String, Object> response) {
        Map<String, Object> data = new HashMap<>();

        try {
            // Simple queries
            if (query.contains("hello")) {
                data.put("hello", "Hello from GraphQL Controller!");
            }

            if (query.contains("getUsers")) {
                data.put("getUsers", new ArrayList<>(users.values()));
            }

            if (query.contains("getProducts")) {
                data.put("getProducts", new ArrayList<>(products.values()));
            }

            // Single item queries
            if (query.contains("getUser")) {
                String userId = extractIdFromQuery(query, "getUser", variables);
                if (userId != null) {
                    Map<String, Object> user = users.get(userId);
                    if (user != null) {
                        data.put("getUser", user);
                    } else {
                        addError(response, "User not found with id: " + userId);
                    }
                }
            }

            if (query.contains("getProduct")) {
                String productId = extractIdFromQuery(query, "getProduct", variables);
                if (productId != null) {
                    Map<String, Object> product = products.get(productId);
                    if (product != null) {
                        data.put("getProduct", product);
                    } else {
                        addError(response, "Product not found with id: " + productId);
                    }
                }
            }

            response.put("data", data);

        } catch (Exception e) {
            response.put("data", null);
            response.put("errors", "Query processing error: " + e.getMessage());
        }
    }

    private void processMutation(String query, Map<String, Object> variables, Map<String, Object> response) {
        Map<String, Object> data = new HashMap<>();

        try {
            // Create mutations
            if (query.contains("createUser")) {
                String name = extractArgument(query, "createUser", "name", variables);
                String email = extractArgument(query, "createUser", "email", variables);

                if (name != null && email != null) {
                    String newId = String.valueOf(userIdCounter.incrementAndGet());
                    Map<String, Object> newUser = Map.of(
                            "id", newId,
                            "name", name,
                            "email", email
                    );
                    users.put(newId, newUser);
                    data.put("createUser", newUser);
                } else {
                    addError(response, "Name and email are required for createUser");
                }
            }

            if (query.contains("createProduct")) {
                String name = extractArgument(query, "createProduct", "name", variables);
                Double price = extractNumericArgument(query, "createProduct", "price", variables);

                if (name != null && price != null) {
                    String newId = String.valueOf(productIdCounter.incrementAndGet());
                    Map<String, Object> newProduct = Map.of(
                            "id", newId,
                            "name", name,
                            "price", price,
                            "category", "General" // Default category
                    );
                    products.put(newId, newProduct);
                    data.put("createProduct", newProduct);
                } else {
                    addError(response, "Name and price are required for createProduct");
                }
            }

            // Update mutations
            if (query.contains("updateUser")) {
                String userId = extractIdFromQuery(query, "updateUser", variables);
                if (userId != null && users.containsKey(userId)) {
                    Map<String, Object> user = new HashMap<>(users.get(userId));

                    String name = extractArgument(query, "updateUser", "name", variables);
                    String email = extractArgument(query, "updateUser", "email", variables);

                    if (name != null) user.put("name", name);
                    if (email != null) user.put("email", email);

                    users.put(userId, user);
                    data.put("updateUser", user);
                } else {
                    addError(response, "User not found for update");
                }
            }

            if (query.contains("updateProduct")) {
                String productId = extractIdFromQuery(query, "updateProduct", variables);
                if (productId != null && products.containsKey(productId)) {
                    Map<String, Object> product = new HashMap<>(products.get(productId));

                    String name = extractArgument(query, "updateProduct", "name", variables);
                    Double price = extractNumericArgument(query, "updateProduct", "price", variables);

                    if (name != null) product.put("name", name);
                    if (price != null) product.put("price", price);

                    products.put(productId, product);
                    data.put("updateProduct", product);
                } else {
                    addError(response, "Product not found for update");
                }
            }

            // Delete mutations
            if (query.contains("deleteUser")) {
                String userId = extractIdFromQuery(query, "deleteUser", variables);
                if (userId != null) {
                    boolean deleted = users.remove(userId) != null;
                    data.put("deleteUser", deleted);
                } else {
                    addError(response, "User ID is required for deletion");
                }
            }

            if (query.contains("deleteProduct")) {
                String productId = extractIdFromQuery(query, "deleteProduct", variables);
                if (productId != null) {
                    boolean deleted = products.remove(productId) != null;
                    data.put("deleteProduct", deleted);
                } else {
                    addError(response, "Product ID is required for deletion");
                }
            }

            response.put("data", data);

        } catch (Exception e) {
            response.put("data", null);
            response.put("errors", "Mutation processing error: " + e.getMessage());
        }
    }

    private String extractIdFromQuery(String query, String operation, Map<String, Object> variables) {
        // Simple extraction - in real implementation, use a proper GraphQL parser
        try {
            if (variables != null && variables.containsKey("id")) {
                return variables.get("id").toString();
            }

            // Basic string parsing for ID extraction
            int start = query.indexOf(operation + "(id:");
            if (start != -1) {
                start += operation.length() + 4; // Move past "operation(id:"
                int end = query.indexOf(")", start);
                if (end != -1) {
                    String idPart = query.substring(start, end).trim();
                    if (idPart.startsWith("\"") && idPart.endsWith("\"")) {
                        return idPart.substring(1, idPart.length() - 1);
                    }
                    return idPart;
                }
            }
        } catch (Exception e) {
            // Fall through to return null
        }
        return null;
    }

    private String extractArgument(String query, String operation, String argName, Map<String, Object> variables) {
        try {
            if (variables != null && variables.containsKey(argName)) {
                return variables.get(argName).toString();
            }

            // Basic string parsing for argument extraction
            String searchPattern = operation + "(";
            int start = query.indexOf(searchPattern);
            if (start != -1) {
                start += searchPattern.length();
                int end = query.indexOf(")", start);
                if (end != -1) {
                    String argsPart = query.substring(start, end);
                    String[] args = argsPart.split(",");
                    for (String arg : args) {
                        if (arg.trim().startsWith(argName + ":")) {
                            String value = arg.trim().substring(argName.length() + 1).trim();
                            if (value.startsWith("\"") && value.endsWith("\"")) {
                                return value.substring(1, value.length() - 1);
                            }
                            return value;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Fall through to return null
        }
        return null;
    }

    private Double extractNumericArgument(String query, String operation, String argName, Map<String, Object> variables) {
        try {
            if (variables != null && variables.containsKey(argName)) {
                Object value = variables.get(argName);
                if (value instanceof Number) {
                    return ((Number) value).doubleValue();
                }
                return Double.parseDouble(value.toString());
            }

            String stringValue = extractArgument(query, operation, argName, variables);
            if (stringValue != null) {
                return Double.parseDouble(stringValue);
            }
        } catch (Exception e) {
            // Fall through to return null
        }
        return null;
    }

    private void addError(Map<String, Object> response, String errorMessage) {
        @SuppressWarnings("unchecked")
        List<String> errors = (List<String>) response.getOrDefault("errors", new ArrayList<String>());
        errors.add(errorMessage);
        response.put("errors", errors);
    }

    @GetMapping("/test")
    public String test() {
        return "GraphQL Controller is working! Use POST /api/graphql for queries and mutations.";
    }

    @GetMapping("/schema")
    public String getSchema() {
        return """
            Available Queries:
            - hello: String
            - getUsers: [User]
            - getProducts: [Product]
            - getUser(id: ID!): User
            - getProduct(id: ID!): Product
            
            Available Mutations:
            - createUser(name: String!, email: String!): User
            - createProduct(name: String!, price: Float!): Product
            - updateUser(id: ID!, name: String, email: String): User
            - updateProduct(id: ID!, name: String, price: Float): Product
            - deleteUser(id: ID!): Boolean
            - deleteProduct(id: ID!): Boolean
            
            Example query: 
            { 
              "query": "query { hello getUsers { id name email } getUser(id: \\\"1\\\") { id name } }" 
            }
            
            Example mutation:
            { 
              "query": "mutation { createUser(name: \\\"John Doe\\\", email: \\\"john@example.com\\\") { id name email } }",
              "variables": { "name": "John Doe", "email": "john@example.com" }
            }
            """;
    }
}