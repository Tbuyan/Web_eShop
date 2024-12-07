package com.eshop.service;

import com.eshop.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    List<Product> alllist();

    //添加商品
    boolean addProduct(Product product);
    //根据商品名删除商品
    boolean deleteProduct(Product product);
    //修改商品
    boolean updateProduct(Product product);
    //根据商品名查询商品
    List<Product> getProduct(String pName);

    List<Product> getProductInShop(String pName, Integer sid);

    List<Product> getProductByType(String pType);

    Product getProductById(Integer pid);

    List<Product> getProductBySaler(Integer uid);
}
