package com.eshop.service;

import com.eshop.domain.Result;
import com.eshop.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
   User login (String uName, String password);

   boolean register(User user);

   boolean reset(User user);
   User getUser(Integer uid);
}
