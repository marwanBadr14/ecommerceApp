package com.gizasystems.inventoryservice.controller;

import com.gizasystems.inventoryservice.dto.CategoryDto;
import com.gizasystems.inventoryservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
// TODO: 11/14/2023 nice controller *clap* *clap*
public class CategoryController {
    // TODO: 11/14/2023 nice to have use constructor injection
    @Autowired
    CategoryService categoryService;
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
