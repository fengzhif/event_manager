package com.fengzhi.event_manager.controller;

import com.fengzhi.event_manager.pojo.Category;
import com.fengzhi.event_manager.pojo.Result;
import com.fengzhi.event_manager.service.impl.CategoryService;
import com.fengzhi.event_manager.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
@Validated
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Result addCategory(@RequestBody @Validated(Category.Add.class) Category category) {
        categoryService.addCategory(category);
        return Result.success();
    }

    @GetMapping
    public Result<List<Category>> getCategoryList() {
        List<Category> categoryList = categoryService.getCategoryList();
        return Result.success(categoryList);
    }

    @GetMapping("/detail")
    public Result<Category> getDetail(@RequestParam("id") Integer id) {
        Category category = categoryService.getDetail(id);
        return Result.success(category);
    }

    @PutMapping
    public Result updateCategory(@RequestBody @Validated(Category.Update.class) Category category) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer updateUserId = (Integer) claims.get("id");
        Integer id = category.getId();
        Category old_ca = categoryService.findCategoryById(id);
        Integer createUserId = old_ca.getCreateUser();
        if (!createUserId.equals(updateUserId)) {
            return Result.error("只有分类创建者可修改分类信息");
        }
        categoryService.updateCategory(category);
        return Result.success();
    }

    @DeleteMapping
    public Result deleteCategory(@RequestParam("id") Integer id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer updateUserId = (Integer) claims.get("id");
        Category old_ca = categoryService.findCategoryById(id);
        Integer createUserId = old_ca.getCreateUser();
        if (!createUserId.equals(updateUserId)) {
            return Result.error("只有分类创建者可删除分类信息");
        }
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
