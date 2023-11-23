package com.gizasystems.inventoryservice.controller;

import com.gizasystems.inventoryservice.dto.CategoryDto;
import com.gizasystems.inventoryservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/categories")
public class CategoryController {

    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
            return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<CategoryDto> addProductCategory(@RequestBody CategoryDto categoryDto){
        return ResponseEntity.ok(categoryService.addCategory(categoryDto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<CategoryDto> editProductCategory(@PathVariable Integer id, @RequestBody CategoryDto categoryDto){
        return ResponseEntity.ok(categoryService.editCategoryById(id, categoryDto));
    }

    @DeleteMapping("delete/{id}")
    public void deleteProductCategory(@PathVariable Integer id){
       categoryService.deleteCategoryById(id);
    }


}
