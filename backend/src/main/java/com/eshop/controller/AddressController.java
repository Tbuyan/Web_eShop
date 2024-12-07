package com.eshop.controller;

import com.eshop.domain.Address;
import com.eshop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("/getuseraddress")
    public List<Address> getUserAddress(@RequestParam Integer uid){
        return addressService.getUserAddress(uid);
    }
    @PostMapping("/adduseraddress")
    public boolean addUserAddress(@RequestBody Address address){
        return addressService.addUserAddress(address);
    }
    @PostMapping("/deleteuseraddress")
    public boolean deleteUserAddress(@RequestBody Address address){
        return addressService.deleteUserAddress(address);
    }
    @PostMapping("/updateuseraddress")
    public boolean updateUserAddress(@RequestBody Address address){
        return addressService.updateUserAddress(address);
    }
}
