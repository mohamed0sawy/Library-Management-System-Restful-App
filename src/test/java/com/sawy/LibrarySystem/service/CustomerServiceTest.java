package com.sawy.LibrarySystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.sawy.LibrarySystem.model.Customer;
import com.sawy.LibrarySystem.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerRepository, passwordEncoder);
    }

    @Test
    @DisplayName("Get All Customers")
    void getAllCustomers_pageSizeSortBySortOrder_PageOfCustomers() {
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String sortOrder = "asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        Page<Customer> customers = new PageImpl<>(List.of(new Customer(), new Customer()));

        when(customerRepository.findAll(pageable)).thenReturn(customers);

        Page<Customer> result = customerService.getAllCustomers(page, size, sortBy, sortOrder);

        assertEquals(customers, result);
        verify(customerRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Get Customer By ID")
    void getCustomerById_ID_Customer() {
        Long id = 1L;
        Customer customer = new Customer();
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomerById(id);

        assertTrue(result.isPresent());
        assertEquals(customer, result.get());
        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Create Customer")
    void createCustomer_Customer_Customer() {
        Customer customer = new Customer();
        customer.setPassword("rawPassword");

        when(passwordEncoder.encode(customer.getPassword())).thenReturn("encodedPassword");
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.createCustomer(customer);

        assertEquals(customer, result);
        assertEquals("encodedPassword", customer.getPassword());
        verify(passwordEncoder, times(1)).encode("rawPassword");
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    @DisplayName("Update Customer")
    void updateCustomer_IDAndCustomerDetails_OptionalCustomer() {
        Long id = 1L;
        Customer existingCustomer = new Customer();
        existingCustomer.setId(id);
        Customer updatedCustomerDetails = new Customer();
        updatedCustomerDetails.setName("New Name");
        updatedCustomerDetails.setPassword("New Password");

        when(customerRepository.findById(id)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);

        Optional<Customer> result = customerService.updateCustomer(id, updatedCustomerDetails);

        assertTrue(result.isPresent());
        assertEquals("New Name", result.get().getName());
        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Delete Customer")
    void deleteCustomer_ID_Void() {
        Long id = 1L;

        customerService.deleteCustomer(id);

        verify(customerRepository, times(1)).deleteById(id);
    }
}