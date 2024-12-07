package com.eshop.service.impl;

import com.eshop.domain.Address;
import com.eshop.mapper.AddressMapper;
import com.eshop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public List<Address> getUserAddress(Integer uid) {
        return addressMapper.getUserAddress(uid);
    }

    @Override
    public boolean addUserAddress(Address address) {
        return addressMapper.addUserAddress(address) == 1;
    }

    @Override
    public boolean deleteUserAddress(Address address) {
        return addressMapper.deleteUserAddress(address) == 1;
    }

    @Override
    public boolean updateUserAddress(Address address) {
        return addressMapper.updateUserAddress(address) == 1;
    }
}
