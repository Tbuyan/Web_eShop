package com.eshop.mapper;

import com.eshop.domain.Product;
import com.eshop.domain.Shopcar;
import com.eshop.domain.ShopcarId;
import org.apache.ibatis.annotations.Mapper;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

@Mapper
public interface ShopcarMapper extends JpaSpecificationExecutor<Shopcar>
{
    List<Product> getUserShopCar(Integer uid);
    Product getProductById(Integer uid0,Integer pId);

    Integer getUserShopCarCount(Integer uid);

    int addUserShopCar(Shopcar shopcar);

    int deleteUserShopCar(Shopcar shopcar);

    int updateUserShopCar(Shopcar shopcar);

    Shopcar findById(Shopcar shopcar);

    Integer getProductCount(Integer uid0, Integer pId);
}
