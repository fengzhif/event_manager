package com.fengzhi.event_manager.mapper;

import com.fengzhi.event_manager.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    User findByName(String username);

    @Insert("insert into user (username,password,create_time,update_time) " +
            " values (#{username},#{password},now(),now())")
    void add(String username, String password);
}
