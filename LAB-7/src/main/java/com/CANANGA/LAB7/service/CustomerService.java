package com.CANANGA.LAB7.service;

import com.CANANGA.LAB7.entity.Customer;
import com.CANANGA.LAB7.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        initializeSampleCustomers();
    }

    private void initializeSampleCustomers() {
        if (customerRepository.count() == 0) {
            customerRepository.save(new Customer("Kim Cananga", "cananga@gmail.com"));
            customerRepository.save(new Customer("Edison Damayo", "damayo@gmail.com"));
            customerRepository.save(new Customer("Shierafel Boticario", "boticario@gmail.com"));
        }
    }

    public List<Customer> fetchAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> fetchCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    // ADD THIS METHOD - Fix for the error
    public Optional<Customer> fetchCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    public Optional<Customer> modifyCustomer(Long id, Customer customerUpdate) {
        return customerRepository.findById(id).map(existingCustomer -> {
            existingCustomer.setName(customerUpdate.getName());
            existingCustomer.setEmail(customerUpdate.getEmail());
            return customerRepository.save(existingCustomer);
        });
    }

    @Transactional
    public boolean removeCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}