package com.fengzhi.event_manager.service.impl;

import com.fengzhi.event_manager.pojo.Category;

import java.util.List;

public interface CategoryService {
    void addCategory(Category category);

    List<Category> getCategoryList();

    Category getDetail(Integer id);

    void updateCategory(Category category);

    Category findCategoryById(Integer id);

    void deleteCategory(Integer id);
}
