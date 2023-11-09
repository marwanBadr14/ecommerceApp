package com.gizasystems.inventoryservice.service;
import com.gizasystems.inventoryservice.dao.CategoryDao;
import com.gizasystems.inventoryservice.dto.CategoryDto;
import com.gizasystems.inventoryservice.exception.ApiRequestException;
import com.gizasystems.inventoryservice.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryDao categoryDao;


    // Retrieve a list of all categories
    public List<Category> getAllCategories() {
        try {
            return categoryDao.findAll();
        }catch (Exception e){
            throw new RuntimeException("Couldn't retrieve all categories");
        }
    }


    // Retrieve a specific category by its id
    public Category getCategoryById(Integer id) {
        Optional<Category> category = categoryDao.findById(id);
        try{
            if (category.isPresent())
                return category.get();
            throw new ApiRequestException("Couldn't find a category with "+id);
        }catch (Exception e){
            throw new ApiRequestException("Couldn't retrieve category #"+id);
        }
    }

    // Add a new category
    public Category addCategory(CategoryDto categoryDto) {
        try{
            Category category = new Category();
            category.setName(categoryDto.getName());
            return categoryDao.save(category);
        }catch (Exception e){
            throw new ApiRequestException("Couldn't add a new category");
        }
    }

    // Edit an already existing category
    public Category editCategoryById(Integer id, CategoryDto categoryDto) {
        try {
            Optional<Category> category = categoryDao.findById(id);
            if (category.isPresent()) {
                Category editedCategory = category.get();
                editedCategory.setId(id);
                if (categoryDto.getName() != null)
                    editedCategory.setName(categoryDto.getName());
                return categoryDao.save(editedCategory);
            }
            throw new ApiRequestException("Couldn't find a category with "+id);
        }
           catch (Exception e){
                throw new ApiRequestException("Couldn't edit category #"+id);
        }
    }

    // Delete category by id
    public void deleteCategoryById(Integer id) {
        try{
            Optional<Category> category = categoryDao.findById(id);
            if(category.isPresent()){
                categoryDao.deleteById(id);
            }
            throw new ApiRequestException("Couldn't find a category with "+id);
        }catch (Exception e){
            throw new ApiRequestException("Couldn't delete category #"+id);
        }
    }
}
