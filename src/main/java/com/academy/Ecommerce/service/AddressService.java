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

    public Address createAddress(Address address, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(" User with ID " + userId + " not found ");
        }
        User user = userOptional.get();
        address.setUser(user);
        return addressRepository.save(address);
    }

    public Address getAddressByIdAndUserId(Long id, Long userId) {
        return addressRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException("Address not found"));
    }

    public void updateAddressByUserId(Address updatedAddress, Long addressId, Long userId) {
        Address existingAddress = getAddressByIdAndUserId(addressId, userId);
        if (existingAddress == null) {
            throw new NotFoundException("Address with ID " + addressId + " not found for User ID " + userId);
        }

        existingAddress.setStreet(updatedAddress.getStreet());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setState(updatedAddress.getState());
        existingAddress.setZipCode(updatedAddress.getZipCode());
        addressRepository.save(existingAddress);
    }

    public String deleteAddressById(Long id){
        Address address = addressRepository.findById(id).orElse(null);
        if(address == null){
            throw new NotFoundException(" No Address found with ID: " + id);
        }
        addressRepository.deleteById(id);
        return " Address with ID " + id + " deleted successfully ";
    }

    public List<Address> getAddressesByUserId(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new NotFoundException("User with ID " + userId + " not found");
        }
        return addressRepository.findByUserId(userId);
    }
}
