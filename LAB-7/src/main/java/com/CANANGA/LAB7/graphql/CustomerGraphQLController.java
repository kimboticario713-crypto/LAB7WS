package com.CANANGA.LAB7.graphql;

import com.CANANGA.LAB7.entity.Customer;
import com.CANANGA.LAB7.service.CustomerService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Optional;

@Controller
public class CustomerGraphQLController {

    private final CustomerService customerService;

    public CustomerGraphQLController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @QueryMapping
    public List<Customer> getUsers() {
        return customerService.fetchAllCustomers();
    }

    @QueryMapping
    public Optional<Customer> getUser(@Argument Long id) {
        return customerService.fetchCustomerById(id);
    }

    @MutationMapping
    public Customer createUser(@Argument String name, @Argument String email) {
        Customer customer = new Customer(name, email);
        return customerService.createCustomer(customer);
    }

    @MutationMapping
    public Optional<Customer> updateUser(
            @Argument Long id,
            @Argument String name,
            @Argument String email) {

        Customer customerUpdate = new Customer(name, email);
        customerUpdate.setId(id);
        return customerService.modifyCustomer(id, customerUpdate);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        return customerService.removeCustomer(id);
    }
}