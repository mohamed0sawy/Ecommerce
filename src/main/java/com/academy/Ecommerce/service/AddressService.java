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

    public Address updateAddressByUserId(Address address, Long userId) {
        // Find all addresses of the user
        List<Address> userAddresses = addressRepository.findByUserId(userId);

        // Update the address in the list (assuming you have the logic to uniquely identify the address)
        for (Address userAddress : userAddresses) {
            if (userAddress.getId().equals(address.getId())) {
                userAddress.setStreet(address.getStreet());
                userAddress.setCity(address.getCity());
                userAddress.setState(address.getState());
                userAddress.setZipCode(address.getZipCode());
                // Optionally, update other fields as needed
                return addressRepository.save(userAddress);
            }
        }
        throw new NotFoundException("Address with ID " + address.getId() + " not found for User ID " + userId);
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
