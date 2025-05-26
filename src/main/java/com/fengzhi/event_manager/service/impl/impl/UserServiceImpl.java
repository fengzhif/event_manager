package com.fengzhi.event_manager.service.impl.impl;

import com.fengzhi.event_manager.mapper.UserMapper;
import com.fengzhi.event_manager.pojo.User;
import com.fengzhi.event_manager.pojo.UserRole;
import com.fengzhi.event_manager.service.impl.UserService;
import com.fengzhi.event_manager.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    UserMapper mapper;
    @Autowired
    public UserServiceImpl(UserMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public User findByName(String username) {
       return  mapper.findByName(username);
    }

    @Override
    public void register(String username, String password) {
        //加密
        String md5String = Md5Util.getMD5String(password);
        // 通过前端注册时，用户属性默认为USER
        mapper.add(username,md5String, UserRole.USER.name());
    }

    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        mapper.update(user);
    }

    @Override
    public void updateAvatar(Integer id, String avatarUrl) {
        mapper.updateAvatar(id,avatarUrl);
    }

    @Override
    public void updatePwd(Integer id, String newPassword) {
        mapper.updatePwd(id,newPassword);
    }
}
