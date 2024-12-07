//package com.eshop.service;
//
//import com.eshop.domain.Product;
//import com.eshop.domain.Shop;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public interface ShopService {
//    //查询店铺商品列表
//    List<Product> getShopProduct(String shopname);
//    //查询店铺信息
//    Shop getShopInfo(String sName);
//    //添加店铺商品
//    boolean addProduct(Product product,String shopname);
//    //获取店铺id
//    Integer getShopId(String sName);
//    //删除店铺商品
//    boolean deleteProduct(Product product,String sName);
//    //更新商品信息
//    boolean updateProduct(Product product,String sName);
//    //模糊查询店铺商品信息
//    List<Product> getProduct(String pName,String sName);
//    //新增店铺
//    boolean addShop(Shop shop);
//    //删除店铺
//    boolean deleteShop(Shop shop);
//    //更新店铺信息
//    boolean updateShop(Shop shop);
//}
