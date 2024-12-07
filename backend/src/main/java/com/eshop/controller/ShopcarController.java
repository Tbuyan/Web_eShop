package com.eshop.controller;

import com.eshop.domain.Product;
import com.eshop.domain.Shopcar;
import com.eshop.service.ShopcarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopcar")
public class ShopcarController {
    @Autowired
    private ShopcarService shopcarService;

    @GetMapping("/getusershopcar")
    public List<Product> getUserShopCar(@RequestParam Integer uid){
        return shopcarService.getUserShopCar(uid);
    }

    @GetMapping("/getproduct")
    public Product getProductById(@RequestParam Integer uid0,@RequestParam Integer pId){
        return shopcarService.getProductById(uid0,pId);
    }
    @GetMapping("/getproductcount")
    public Integer getProductCount(@RequestParam Integer uid0,@RequestParam Integer pId){
        return shopcarService.getProductCount(uid0,pId);
    }
    @GetMapping("/getusershopcarcount")
    public Integer getUserShopCarCount(@RequestParam Integer uid){
        return shopcarService.getUserShopCarCount(uid);
    }
    @PostMapping("/addshopcar")
    public boolean addUserShopCar(@RequestBody Shopcar shopcar){
        System.out.println(shopcar);
        return shopcarService.addUserShopCar(shopcar);
    }
    @PostMapping("/deleteshopcar")
    public boolean deleteUserShopCar(@RequestBody Shopcar shopcar){
        return shopcarService.deleteUserShopCar(shopcar);
    }
    @PostMapping("/updateshopcar")
    public boolean updateUserShopCar(@RequestBody Shopcar shopcar){
        return shopcarService.updateUserShopCar(shopcar);
    }
    @PostMapping("/ordershopcar")
    public boolean orderShopCar(@RequestBody List<Shopcar> shopcarList,@RequestParam Integer oid){
        for (Shopcar shopcar:shopcarList){
            System.out.println(shopcar);
        }
        return shopcarService.orderShopCar(shopcarList,oid);
    }
}
