package com.academy.Ecommerce.service;

import com.academy.Ecommerce.model.Customer;
import com.academy.Ecommerce.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer findCustomerByName(String name){
        return customerRepository.findByName(name);
    }

}
