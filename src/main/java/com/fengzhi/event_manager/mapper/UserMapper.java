package com.fengzhi.event_manager.mapper;

import com.fengzhi.event_manager.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    User findByName(String username);

    @Insert("insert into user (username,password,create_time,update_time) " +
            " values (#{username},#{password},now(),now())")
    void add(String username, String password);

    @Update("update user set email=#{email},nickname=#{nickname},update_time=#{updateTime} where id=#{id}")
    void update(User user);

    @Update("update user set user_pic_url=#{avatarUrl},update_time=now() where id=#{id}")
    void updateAvatar(Integer id, String avatarUrl);

    @Update("update user set password=#{newPassword},update_time=now() where id=#{id}")
    void updatePwd(Integer id, String newPassword);
}
