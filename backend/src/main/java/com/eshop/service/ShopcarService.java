package com.eshop.service;

import com.eshop.domain.Product;
import com.eshop.domain.Shopcar;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShopcarService {
    List<Product> getUserShopCar(Integer uid);

    Product getProductById(Integer uid0, Integer pId);

    Integer getUserShopCarCount(Integer uid);

    boolean addUserShopCar(Shopcar shopcar);

    boolean deleteUserShopCar(Shopcar shopcar);

    boolean updateUserShopCar(Shopcar shopcar);

    boolean orderShopCar(List<Shopcar> shopcarList, Integer oid);

    Integer getProductCount(Integer uid0, Integer pId);


}
