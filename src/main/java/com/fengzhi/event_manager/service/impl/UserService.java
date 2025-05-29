package com.fengzhi.event_manager.service.impl;

import com.fengzhi.event_manager.pojo.User;
import org.hibernate.validator.constraints.URL;

public interface UserService {
    User findByName(String username);

    void register(String username, String password);

    void update(User user);

    void updateAvatar(Integer id, @URL String avatarUrl);

    void updatePwd(Integer id, String md5String);


}
