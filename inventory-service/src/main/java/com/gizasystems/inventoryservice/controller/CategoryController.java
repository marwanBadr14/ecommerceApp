package com.gizasystems.inventoryservice.controller;

import com.gizasystems.inventoryservice.dto.CategoryDto;
import com.gizasystems.inventoryservice.model.Category;
import com.gizasystems.inventoryservice.service.CategoryService;
//import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    @GetMapping("/all")
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Integer id){
        return categoryService.getCategoryById(id);
    }

    @PostMapping("/add")
    public Category addProductCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }

    @PutMapping("/edit/{id}")
    public Category editProductCategory(@PathVariable Integer id, @RequestBody CategoryDto categoryDto){
        return categoryService.editCategoryById(id, categoryDto);
    }

    @DeleteMapping("delete/{id}")
    public void deleteProductCategory(@PathVariable Integer id){
       categoryService.deleteCategoryById(id);
    }


}
