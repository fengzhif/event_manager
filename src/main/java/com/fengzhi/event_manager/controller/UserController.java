package com.fengzhi.event_manager.controller;

import com.fengzhi.event_manager.pojo.Result;
import com.fengzhi.event_manager.pojo.User;
import com.fengzhi.event_manager.service.impl.EmailService;
import com.fengzhi.event_manager.service.impl.UserService;
import com.fengzhi.event_manager.utils.JwtUtil;
import com.fengzhi.event_manager.utils.Md5Util;
import com.fengzhi.event_manager.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private final View error;
    UserService userService;
    StringRedisTemplate stringRedisTemplate;
    @Value("${frontend.url}")
    private String frontendUrl;
    private EmailService emailService;

    @Autowired
    public UserController(UserService userService, StringRedisTemplate stringRedisTemplate, EmailService emailService, View error) {
        this.userService = userService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.emailService = emailService;
        this.error = error;
    }

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        User user = userService.findByName(username);
        if (user == null) {
            userService.register(username, password);
            return Result.success();
        } else {
            return Result.error("用户名已存在");
        }

    }

    @PostMapping("login")
    public Result login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        User user = userService.findByName(username);
        if (user == null) {
            return Result.error("用户不存在");
        } else {
            if (Md5Util.checkPassword(password, user.getPassword())) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("username", username);
                claims.put("id", user.getId());
                claims.put("role", user.getRole().name());
                String token = JwtUtil.generateToken(claims);
                //token存入Redis
                stringRedisTemplate.opsForValue().set(token, token,1, TimeUnit.HOURS);
                return Result.success(token);
            } else {
                return Result.error("密码错误");
            }
        }
    }

    @GetMapping("/userInfo")
    public Result userInfo() {
        Map<String, Object> claim = ThreadLocalUtil.get();
        String userName = (String) claim.get("username");
        User user = userService.findByName(userName);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam("avatarUrl") @URL String avatarUrl) {
        Map<String, Object> claim = ThreadLocalUtil.get();
        Integer id = (Integer) claim.get("id");
        userService.updateAvatar(id, avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token) {
        String old_pwd = params.get("old_pwd");
        String new_pwd = params.get("new_pwd");
        String re_pwd = params.get("re_pwd");
        //检验参数数量
        if (old_pwd.isEmpty() || new_pwd.isEmpty() || re_pwd.isEmpty()) {
            return Result.error("缺少必要参数");
        }
        //判断原密码是否正确
        Map<String, Object> claim = ThreadLocalUtil.get();
        String userName = (String) claim.get("username");
        Integer id = (Integer) claim.get("id");
        User user = userService.findByName(userName);
        if (!Md5Util.checkPassword(old_pwd, user.getPassword())) {
            return Result.error("原密码错误");
        }
        //判断新密码与重复密码是否相等
        if (!new_pwd.equals(re_pwd)) {
            return Result.error("两次新密码不相等");
        }
        //判读新密码是否与原密码相等
        if (new_pwd.equals(old_pwd)) {
            return Result.error("新密码不能与原密码相等");
        }
        //判断新密码是否符合格式需求
        if (!new_pwd.matches("^\\S{5,16}$")) {
            return Result.error("新密码格式不对，应为5-16个非空字符");
        }

        userService.updatePwd(id, Md5Util.getMD5String(new_pwd));
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }

    @PostMapping("/forgotPassword")
    public Result forgotPassword(@RequestParam String username) {
        User user = userService.findByName(username);
        if (user == null) {
            return Result.error("用户名不存在");
        }
        String email = user.getEmail();
        if (email.isEmpty()) {
            return Result.error("该用户未设置找回邮箱");
        }
        //获取重置token和过期时间
        String token = UUID.randomUUID().toString();
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(30);
        user.setResetPasswordToken(token);
        user.setResetPasswordExpireTime(expireTime);
        user.setUpdateTime(LocalDateTime.now());
        userService.update(user);

        String resetLink = "https://" + frontendUrl + "/resetPassword?token=" + token+"&username=" + username;

        emailService.sendSimpleMessage(
                email,
                "重置密码请求",
                "点击以下链接重置密码（30分钟内有效）：\n" + resetLink
        );
        return Result.success("重置密码邮件已发送，请检查邮箱");
    }

    @PatchMapping("/resetPassword")
    public Result resetPassword(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String token = params.get("token");
        String new_pwd = params.get("new_pwd");
        String re_pwd = params.get("re_pwd");
        if(username == null || new_pwd == null || re_pwd == null || token == null) {
            Result.error("缺少必要参数");
        }
        if (!new_pwd.equals(re_pwd)) {
            return Result.error("两次新密码不相等");
        }
        if (!new_pwd.matches("^\\S{5,16}$")) {
            return Result.error("新密码格式不对，应为5-16个非空字符");
        }
        User user = userService.findByName(username);
        if(user == null) {
            return Result.error("用户不存在");
        }
        if(user.getResetPasswordExpireTime().isBefore(LocalDateTime.now())
                || !user.getResetPasswordToken().equals(token)) {
            return Result.error("无效或过期的重置链接");
        }
        user.setResetPasswordToken(null);
        user.setResetPasswordExpireTime(null);
        user.setPassword(Md5Util.getMD5String(new_pwd));
        user.setUpdateTime(LocalDateTime.now());
        userService.update(user);
        return Result.success("密码重置成功");
    }
}
