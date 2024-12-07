package com.eshop.service.impl;


import com.eshop.domain.User;
import com.eshop.mapper.UserMapper;
import com.eshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User login(String uName, String password) {
        User user= userMapper.login(uName, password);
        return user;
    }

    @Override
    public boolean register(User user) {
        return userMapper.register(user)!=0;
    }

    @Override
    public boolean reset(User user) {
        return userMapper.reset(user)!=0;
    }

    @Override
    public User getUser(Integer uid) {
        return userMapper.getUser(uid);
    }
}
