package com.gizasystems.inventoryservice.service;

import com.gizasystems.inventoryservice.dao.CategoryDao;
import com.gizasystems.inventoryservice.dto.CategoryDto;
import com.gizasystems.inventoryservice.exception.CategoryNotFoundException;
import com.gizasystems.inventoryservice.mapper.CategoryMapper;
import com.gizasystems.inventoryservice.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryMapper categoryMapper;


    // Retrieve a list of all categories
    public List<CategoryDto> getAllCategories() {
        List<CategoryDto> categoryDtos = categoryMapper.transferToDto(categoryDao.findAll());
        if (categoryDtos == null)
            throw new CategoryNotFoundException("Couldn't retrieve categories");
        return categoryDtos;
    }


    // Retrieve a specific category by its id
    public CategoryDto getCategoryById(Integer id) {
        Optional<Category> category = categoryDao.findById(id);
        if (category.isEmpty())
            throw new CategoryNotFoundException("Couldn't find a category with " + id);
        return categoryMapper.transferToDto(categoryDao.findById(id).orElse(null));
    }

    // Add a new category
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        categoryDao.save(category);
        return categoryMapper.transferToDto(category);
    }

    // Edit an already existing category
    public CategoryDto editCategoryById(Integer id, CategoryDto categoryDto) {

        Optional<Category> category = categoryDao.findById(id);
        if (category.isEmpty())
            throw new CategoryNotFoundException("Couldn't find a category with " + id);

        Category editedCategory = category.get();
        editedCategory.setId(id);
        if (categoryDto.getName() != null)
            editedCategory.setName(categoryDto.getName());

        categoryDao.save(editedCategory);
        return categoryMapper.transferToDto(editedCategory);

    }

    // Delete category by id
    public void deleteCategoryById(Integer id) {
        Optional<Category> category = categoryDao.findById(id);
        if (category.isEmpty())
            throw new CategoryNotFoundException("Couldn't find a category with " + id);

        categoryDao.deleteById(id);
    }

}
