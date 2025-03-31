package com.fengzhi.event_manager.controller;

import com.fengzhi.event_manager.pojo.Result;
import com.fengzhi.event_manager.pojo.User;
import com.fengzhi.event_manager.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result register(String username,String password) {
        User user=userService.findByName(username);
        if(user==null) {
            userService.register(username,password);
            return Result.success();
        }else{
            return Result.error("用户名已存在");
        }

    }
    @GetMapping("/hello")
    public Result hello() {
        return Result.success();
    }
}
