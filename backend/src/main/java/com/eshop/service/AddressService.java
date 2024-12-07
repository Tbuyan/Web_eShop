package com.eshop.service;

import com.eshop.domain.Address;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
    List<Address> getUserAddress(Integer uid);

    boolean addUserAddress(Address address);

    boolean deleteUserAddress(Address address);

    boolean updateUserAddress(Address address);
}
