package com.academy.Ecommerce.service;

import com.academy.Ecommerce.exception.NotFoundException;
import com.academy.Ecommerce.model.Address;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.repository.AddressRepository;
import com.academy.Ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Address> getAllAddresses(){
        return addressRepository.findAll();
    }

    public Address getAddressById(Long id){
        Address address = addressRepository.findById(id).orElse(null);
        if(address == null){
            throw new NotFoundException(" No Address found with ID: " +id);
        }
        return address;
    }

    public Address createAddress(Address address){
        Long userId = address.getUser().getId();
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new NotFoundException(" User with ID " + userId + " not found ");
        }
        User user = userOptional.get();
        address.setUser(user);
        return addressRepository.save(address);
    }

    public Address updateAddressById(Address address, Long id){
        Optional<Address> existingAddressOptional = addressRepository.findById(id);
        if(existingAddressOptional.isPresent()){
            Address existingAddress = existingAddressOptional.get();
            existingAddress.setId(id);
            return addressRepository.save(existingAddress);
        }
        else{
            throw new NotFoundException(" No Address found with ID: " + id);
        }
    }

    public String deleteAddressById(Long id){
        Address address = addressRepository.findById(id).orElse(null);
        if(address == null){
            throw new NotFoundException(" No Address found with ID: " + id);
        }
        addressRepository.deleteById(id);
        return " Book with ID " + id + " deleted successfully ";
    }
}
