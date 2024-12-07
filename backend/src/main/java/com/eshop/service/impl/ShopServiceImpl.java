//package com.eshop.service.impl;
//
//import com.eshop.domain.Product;
//import com.eshop.domain.Shop;
//import com.eshop.mapper.ShopMapper;
//import com.eshop.mapper.ProductMapper;
//import com.eshop.service.ShopService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ShopServiceImpl implements ShopService {
//    @Autowired
//    ShopMapper shopmapper;
//    @Autowired
//    ProductMapper productmapper;
//    @Override
//    public List<Product> getShopProduct(String shopname) {
//        return shopmapper.getShopProduct(shopname);
//    }
//
//    @Override
//    public Shop getShopInfo(String sName) {
//        return shopmapper.getShopInfo(sName);
//    }
//
//    @Override
//    public boolean addProduct(Product product, String shopname) {
//        product.setSid(getShopId(shopname));
//        return productmapper.addProduct(product)==1;
//    }
//
//    @Override
//    public Integer getShopId(String sName) {
//        return shopmapper.getShopId(sName);
//    }
//
//    @Override
//    public boolean deleteProduct(Product product, String sName) {
//        product.setSid(getShopId(sName));
//        return productmapper.deleteProduct(product);
//    }
//
//    @Override
//    public boolean updateProduct(Product product, String sName) {
//        product.setSid(getShopId(sName));
//        return productmapper.updateProduct(product).equals(1);
//    }
//
//    @Override
//    public List<Product> getProduct(String pName, String sName) {
//        Integer sid=getShopId(sName);
//        return productmapper.getProductInShop(pName,sid);
//    }
//
//    @Override
//    public boolean addShop(Shop shop) {
//        return shopmapper.addShop(shop)==1;
//    }
//
//    @Override
//    public boolean deleteShop(Shop shop) {
//        return shopmapper.deleteShop(shop);
//    }
//
//    @Override
//    public boolean updateShop(Shop shop) {
//        return shopmapper.updateShop(shop)==1;
//    }
//}
