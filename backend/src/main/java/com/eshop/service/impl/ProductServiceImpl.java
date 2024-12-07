package com.eshop.service.impl;

import com.eshop.domain.Product;
import com.eshop.mapper.ProductMapper;
import com.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Override
    public List<Product> alllist() {
        return productMapper.alllist();
    }

    @Override
    public boolean addProduct(Product product) {
        return productMapper.addProduct(product)==1;
    }

    @Override
    public boolean deleteProduct(Product product) {
        return productMapper.deleteProduct(product)==1;
    }

    @Override
    public boolean updateProduct(Product product) {
        return productMapper.updateProduct(product).equals(1);
    }

    @Override
    public List<Product> getProduct(String pName) {
        return productMapper.getProduct(pName);
    }

    @Override
    public List<Product> getProductInShop(String pName, Integer sid) {
        return productMapper.getProductInShop(pName, sid);
    }

    @Override
    public List<Product> getProductByType(String pType) {
        return productMapper.getProductByType(pType);
    }

    @Override
    public Product getProductById(Integer pid) {
        return productMapper.getProductById(pid);
    }

    @Override
    public List<Product> getProductBySaler(Integer uid) {
        return productMapper.getProductBySaler(uid);
    }
}
