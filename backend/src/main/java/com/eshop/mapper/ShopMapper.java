package com.eshop.mapper;

import com.eshop.domain.Product;
import com.eshop.domain.Shop;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShopMapper {
    List<Product> getShopProduct(String shopname);

    Shop getShopInfo(String sName);

    Integer getShopId(String sName);


    int addShop(Shop shop);

    boolean deleteShop(Shop shop);

    int updateShop(Shop shop);
}
