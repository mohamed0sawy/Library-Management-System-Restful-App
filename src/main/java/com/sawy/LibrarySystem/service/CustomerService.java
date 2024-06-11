package com.sawy.LibrarySystem.service;

import com.sawy.LibrarySystem.model.Customer;
import com.sawy.LibrarySystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<Customer> getAllCustomers(int page, int size, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return customerRepository.findAll(pageable);
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    public Optional<Customer> updateCustomer(Long id, Customer customerDetails) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setName(customerDetails.getName());
            customer.setEmail(customerDetails.getEmail());
            customer.setAddress(customerDetails.getAddress());
            customer.setPhoneNumber(customerDetails.getPhoneNumber());
            customer.setPassword(customerDetails.getPassword());
            return Optional.of(customerRepository.save(customer));
        } else {
            return Optional.empty();
        }
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
