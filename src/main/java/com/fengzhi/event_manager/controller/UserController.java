package com.fengzhi.event_manager.controller;

import com.auth0.jwt.JWT;
import com.fengzhi.event_manager.pojo.Result;
import com.fengzhi.event_manager.pojo.User;
import com.fengzhi.event_manager.service.impl.UserService;
import com.fengzhi.event_manager.utils.JwtUtil;
import com.fengzhi.event_manager.utils.Md5Util;
import com.fengzhi.event_manager.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$")  String password) {
        User user=userService.findByName(username);
        if(user==null) {
            userService.register(username,password);
            return Result.success();
        }else{
            return Result.error("用户名已存在");
        }

    }

    @PostMapping("login")
    public Result login(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$")  String password) {
        User user=userService.findByName(username);
        if(user==null) {
            return Result.error("用户不存在");
        }else{
          if(Md5Util.checkPassword(password,user.getPassword())) {
                Map<String,Object> claims=new HashMap<>();
                claims.put("username",username);
                claims.put("id",user.getId());
                String token= JwtUtil.generateToken(claims);
                return Result.success(token);
          }else {
              return Result.error("密码错误");
          }
        }
    }

    @GetMapping("/userInfo")
    public Result userInfo() {
        Map<String ,Object> claim= ThreadLocalUtil.get();
        String userName=(String)claim.get("username");
        User user=userService.findByName(userName);
        return Result.success(user);
    }
}
