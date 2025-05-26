package com.fengzhi.event_manager.service.impl.impl;

import com.fengzhi.event_manager.mapper.CategoryMapper;
import com.fengzhi.event_manager.pojo.Category;
import com.fengzhi.event_manager.service.impl.CategoryService;
import com.fengzhi.event_manager.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public void addCategory(Category category) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer createUserId = (Integer) claims.get("id");
        category.setCreateUser(createUserId);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.add(category);
    }

    @Override
    public List<Category> getCategoryList() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer createUserId = (Integer) claims.get("id");
        String userRole = (String) claims.get("role");
        if(userRole.equals("ADMIN")){
            return categoryMapper.getAllCategoryList();
        }
        return categoryMapper.getCategoryList(createUserId);
    }


    @Override
    public Category getDetail(Integer id) {
        return categoryMapper.getDetail(id);
    }

    @Override
    public void updateCategory(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.updateCategory(category);
    }

    @Override
    public Category findCategoryById(Integer id) {
        return categoryMapper.findCategoryById(id);
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryMapper.deleteCategory(id);
    }
}
