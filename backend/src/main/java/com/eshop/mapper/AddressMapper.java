package com.eshop.mapper;

import com.eshop.domain.Address;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressMapper {
    List<Address> getUserAddress(Integer uid);

    int addUserAddress(Address address);

    int deleteUserAddress(Address address);

    int updateUserAddress(Address address);
}
