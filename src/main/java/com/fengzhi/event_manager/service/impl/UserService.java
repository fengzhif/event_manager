package com.fengzhi.event_manager.service.impl;

import com.fengzhi.event_manager.pojo.User;

public interface UserService {
    User findByName(String username);

    void register(String username, String password);
}
