package com.fengzhi.event_manager.service.impl.impl;

import com.fengzhi.event_manager.mapper.UserMapper;
import com.fengzhi.event_manager.pojo.User;
import com.fengzhi.event_manager.service.impl.UserService;
import com.fengzhi.event_manager.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        mapper.add(username,md5String);
    }
}
