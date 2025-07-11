package com.fengzhi.event_manager.mapper;

import com.fengzhi.event_manager.pojo.Event;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EventMapper {
    @Insert("insert into event (title,content,cover_img,state,category_id,create_user,create_time,update_time,event_date)" +
            " values(#{title},#{content},#{coverImg},#{state},#{categoryId},#{createUser},#{createTime},#{updateTime},#{eventDate})")
    void add(Event event);

    //动态SQL
    List<Event> getEventList(Integer id, Integer categoryId, String state, LocalDate startDate, LocalDate endDate);
    List<Event> getAllEventList(Integer categoryId, String state, LocalDate startDate, LocalDate endDate);

    @Select("select * from event where id=#{id}")
    Event findEventById(Integer id);

    @Update("update event set title=#{title},content=#{content},cover_img=#{coverImg},state=#{state}," +
            "category_id=#{categoryId},update_time=#{updateTime},event_date=#{eventDate} where id=#{id}")
    void updateEvent(Event event);

    @Delete("delete from event where id=#{id}")
    void deleteEvent(Integer id);
}
