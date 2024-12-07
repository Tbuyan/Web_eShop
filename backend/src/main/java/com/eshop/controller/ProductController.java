package com.eshop.controller;

import com.eshop.domain.Product;
import com.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    // 获取全部商品列表
    @GetMapping("/alllist")
    public List<Product> alllist(){
        System.out.println("alllist");
        return productService.alllist();
    }
    // 根据商品类型获取商品列表
    @GetMapping("/typelist")
    public List<Product> typelist(@RequestParam String pType){
        System.out.println("typelist");
        return productService.getProductByType(pType);
    }
    @GetMapping("/getproduct")
    public List<Product> getProduct(@RequestParam String pName){
        System.out.println("getproduct");
        return productService.getProduct(pName);
    }
    @GetMapping("/getproductbyid")
    public Product getProductById(@RequestParam Integer pid){
        System.out.println(pid);
        System.out.println("getproductbyid");
        return productService.getProductById(pid);
    }
    @PostMapping("/addproduct")
    public boolean addProduct(@RequestBody Product product){
        System.out.println(product);
        return productService.addProduct(product);
    }
    @PostMapping("/deleteproduct")
    public boolean deleteProduct(@RequestBody Product product){
        System.out.println(product);
        return productService.deleteProduct(product);
    }
    @PostMapping("/updateproduct")
    public boolean updateProduct(@RequestBody Product product){
        System.out.println(product);
        return productService.updateProduct(product);
    }
    @GetMapping("/getproductbysaler")
    public List<Product> getProductBySaler(@RequestParam Integer uid){
        System.out.println(uid);
        return productService.getProductBySaler(uid);
    }



}
