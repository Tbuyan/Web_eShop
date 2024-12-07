package com.eshop.mapper;

import com.eshop.domain.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
     List<Product> alllist() ;

    int addProduct(Product product);

    int deleteProduct(Product product);

    Integer updateProduct(Product product);

    List<Product> getProduct(String pName);

    List<Product> getProductInShop(String pName, Integer sid);

    List<Product> getProductByType(String pType);
    Product getProductById(Integer pid);

    List<Product> getProductBySaler(Integer uid);
}
