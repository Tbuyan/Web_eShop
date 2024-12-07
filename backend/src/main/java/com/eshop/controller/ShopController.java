//package com.eshop.controller;
//
//import com.eshop.domain.Product;
//import com.eshop.domain.Shop;
//import com.eshop.service.ShopService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/shop")
//public class ShopController {
//    @Autowired
//    ShopService shopService;
//    //根据店铺名查询店铺所有商品
//    @PostMapping("/shopproduct")
//    public List<Product> getShopProduct(@RequestBody Shop shopname)
//    {
//        return shopService.getShopProduct(shopname.getSName());
//    }
//    //根据店铺名查询店铺信息
//    @PostMapping("/shopinfo")
//    public Shop getShopInfo(@RequestBody Shop shopname)
//    {
//        return shopService.getShopInfo(shopname.getSName());
//    }
//    //添加商品
//    @PostMapping("/addproduct")
//    public boolean addProduct(@RequestBody Product product,@RequestParam String sName)
//    {
//        return shopService.addProduct(product,sName);
//    }
//    //删除商品
//    @PostMapping("/deleteproduct")
//    public boolean deleteProduct(@RequestBody Product product,@RequestParam String sName)
//    {
//        return shopService.deleteProduct(product,sName);
//    }
//    //修改商品
//    @PostMapping("/updateproduct")
//    public boolean updateProduct(@RequestBody Product product,@RequestParam String sName)
//    {
//        return shopService.updateProduct(product,sName);
//    }
//    //根据商品名和店铺名查询商品
//
//    @PostMapping("/getproduct")
//    public List<Product> getProduct(@RequestBody Product product,@RequestParam String sName)
//    {
//        return shopService.getProduct(product.getPName(),sName);
//    }
//    //新增店铺
//    @PostMapping("/addshop")
//    public boolean addShop(@RequestBody Shop shop)
//    {
//        return shopService.addShop(shop);
//    }
//    //删除店铺
//    @PostMapping("/deleteshop")
//    public boolean deleteShop(@RequestBody Shop shop)
//    {
//        return shopService.deleteShop(shop);
//    }
//    //修改店铺信息
//    @PostMapping("/updateshop")
//    public boolean updateShop(@RequestBody Shop shop)
//    {
//        return shopService.updateShop(shop);
//    }
//    //根据店铺名查询店铺id
//    @PostMapping("/getshopid")
//    public Integer getShopId(@RequestBody Shop shop)
//    {
//        return shopService.getShopId(shop.getSName());
//    }
//
//}
