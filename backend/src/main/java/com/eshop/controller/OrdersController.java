package com.eshop.controller;

import com.eshop.domain.Orders;
import com.eshop.domain.OrdersDetail;
import com.eshop.domain.User;
import com.eshop.service.EmailService;
import com.eshop.service.OrdersService;
import com.eshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    EmailService emailService;
    @Autowired
    UserService userService;

    @PostMapping("/addorders")
    public Integer addOrders(@RequestBody Orders orders) {
        System.out.println(orders);
        if(ordersService.addOrders(orders)){
            Integer oid= ordersService.getOrdersBytime(orders.getOTime());
            System.out.println(oid);
            return oid;
        }
        return null;
    }
    @GetMapping("/getallorders")
    public List<Orders> getAllOrders() {
        return ordersService.getAllOrders();
    }
    @GetMapping("/getuserorders")
    public List<Orders> getUserOrders(@RequestParam Integer uid) {
        return ordersService.getUserOrders(uid);
    }
    @PostMapping("/deleteorders")
    public boolean deleteOrders(@RequestBody Orders orders) {
        return ordersService.deleteOrders(orders);
    }

    @PostMapping("/addordersdetail")
    public boolean addOrdersDetail(@RequestBody OrdersDetail ordersDetail) {
        System.out.println(ordersDetail);
        return ordersService.addOrdersDetail(ordersDetail);
    }
    @GetMapping("/getordersdetail")
    public List<OrdersDetail> getOrdersDetail(@RequestParam Integer oid) {
        return ordersService.getOrdersDetail(oid);
    }
    @PostMapping("/deleteordersdetail")
    public boolean deleteOrdersDetail(@RequestBody OrdersDetail ordersDetail) {
        return ordersService.deleteOrdersDetail(ordersDetail);
    }
    @PostMapping("/updateorders")
    public boolean updateOrders(@RequestBody Orders orders, @RequestParam Integer uid) {
        if(orders.getOState().equals(1)){
            User user=userService.getUser(uid);
            emailService.sendText("1197096757@qq.com",user.getEmail(),"订单已发货","您的订单已发货，请注意查收");
        }
        return ordersService.updateOrders(orders);
    }
    @GetMapping("/getordersbysaler")
    public List<OrdersDetail> getOrdersBySaler(@RequestParam Integer uid) {
        return ordersService.getOrdersBySaler(uid);
    }
    @PostMapping("/getorderbyorderdetail")
    public Orders getOrdersDetailtime(@RequestBody OrdersDetail ordersDetail) {
        return ordersService.getOrderbyOrdersDetail(ordersDetail);
    }


}
