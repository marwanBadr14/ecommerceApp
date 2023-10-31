package com.gizasystems.inventoryservice.controller;

import com.gizasystems.inventoryservice.entity.Category;
import com.gizasystems.inventoryservice.service.CategoryService;
//import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id){
        return categoryService.getCategoryById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Category> addProductCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Category> editProductCategory(@PathVariable Integer id, @RequestBody Category category){
        return categoryService.editCategoryById(id, category);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Category> deleteProductCategory(@PathVariable Integer id){
        return categoryService.deleteCategoryById(id);
    }


}
