package com.gizasystems.inventoryservice.service;
import com.gizasystems.inventoryservice.dao.CategoryDao;
import com.gizasystems.inventoryservice.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryDao categoryDao;


    // Retrieve a list of all categories
    public ResponseEntity<List<Category>> getAllCategories() {
        try{
            return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Couldn't retrieve all categories");
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }


    // Retrieve a specific category by its id
    public ResponseEntity<Category> getCategoryById(Integer id) {
        try{
            return new ResponseEntity<>(categoryDao.findById(id).get(), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Couldn't retrieve category with id: "+id);
            e.printStackTrace();
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    // Add a new category
    public ResponseEntity<Category> addCategory(Category category) {
        try{
            return new ResponseEntity<>(categoryDao.save(category),HttpStatus.CREATED);
        }catch (Exception e){
            System.out.println("Couldn't add category "+ category.getName());
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    // Edit an already existing category
    public ResponseEntity<Category> editCategoryById(Integer id, Category productCategory) {
        try {
            Optional<Category> category = categoryDao.findById(id);
            if(category.isPresent()){
                Category editedCategory = category.get();
                editedCategory.setId(id);
                if (productCategory.getName()!=null)
                    editedCategory.setName(productCategory.getName());
                if(productCategory.getProductsId()!=null)
                    editedCategory.setProductsId(productCategory.getProductsId());
                return new ResponseEntity<>(categoryDao.save(editedCategory),HttpStatus.OK);
            }
        }catch (Exception e){
            System.out.println("Couldn't edit category with id: "+id);
            e.printStackTrace();
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    // Delete category by id
    public ResponseEntity<Category> deleteCategoryById(Integer id) {
            Optional<Category> category = categoryDao.findById(id);
        try{
            if(category.isPresent()){
                categoryDao.deleteById(id);
                return new ResponseEntity<>(null,HttpStatus.OK);
            }
        }catch (Exception e){
            System.out.println("Couldn't delete category "+category.get().getName());
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
