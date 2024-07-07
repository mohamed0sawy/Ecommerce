package com.academy.Ecommerce.service;

import com.academy.Ecommerce.model.Address;
import com.academy.Ecommerce.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public List<Address> findAddressByUserId(Long userId){
        return addressRepository.findByUserId(userId);
    }
}
