package com.eshop.controller;

import com.eshop.domain.ShopExp;
import com.eshop.service.ShopExpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopexp")
public class ShopexpController {
    @Autowired
    private ShopExpService shopExpService;
    @PostMapping("/addshopexp")
    public boolean addShopExp(@RequestBody ShopExp shopExp){
        System.out.println(shopExp);
        return shopExpService.addShopExp(shopExp);
    }
    @GetMapping("/getshopexpbyuser")
    public List<ShopExp> getShopExpByUser(@RequestParam Integer uid){
        return shopExpService.getShopExpByUser(uid);
    }
    @GetMapping("/getsalershopexp")
    public List<ShopExp> getSalerShopExp(@RequestParam Integer uid){
        return shopExpService.getSalerShopExp(uid);
    }


}
