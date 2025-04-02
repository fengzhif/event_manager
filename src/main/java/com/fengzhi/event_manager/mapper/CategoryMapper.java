package com.fengzhi.event_manager.mapper;

import com.fengzhi.event_manager.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Insert("insert into category (category_name,category_alias,create_user,create_time,update_time)" +
            " values (#{categoryName},#{categoryAlias},#{createUser},#{createTime},#{updateTime})")
    void add(Category category);

    @Select("select * from category where create_user=#{id}")
    List<Category> getCategoryList(Integer id);

    @Select("select * from category where id=#{id}")
    Category getDetail(Integer id);

    @Update("update category set category_name=#{categoryName},category_alias=#{categoryAlias},update_time=#{updateTime}" +
            " where id=#{id}")
    void updateCategory(Category category);

    @Select("select * from category where id=#{id}")
    Category findCategoryById(Integer id);

    @Delete("delete from category where id=#{id}")
    void deleteCategory(Integer id);
}
