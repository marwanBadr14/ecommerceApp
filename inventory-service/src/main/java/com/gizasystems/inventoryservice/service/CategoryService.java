package com.gizasystems.inventoryservice.service;


import com.gizasystems.inventoryservice.dao.CategoryDao;
import com.gizasystems.inventoryservice.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryDao categoryDao;
    public ResponseEntity<List<ProductCategory>> getAllCategories() {

        try{
            return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ProductCategory> getCategoryById(Integer id) {
        try{
            return new ResponseEntity<>(categoryDao.findById(id).get(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
}
