package com.eshop.mapper;

import com.eshop.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User login(String uName, String password);

    Integer register(User user);

    int reset(User user);

    User getUser(Integer uid);
}
