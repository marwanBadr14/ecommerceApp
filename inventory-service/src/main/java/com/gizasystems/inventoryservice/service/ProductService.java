package com.gizasystems.inventoryservice.service;

import com.gizasystems.inventoryservice.dao.CategoryDao;
import com.gizasystems.inventoryservice.dao.ProductDao;
import com.gizasystems.inventoryservice.dto.ProductDto;
import com.gizasystems.inventoryservice.exception.ApiRequestException;
import com.gizasystems.inventoryservice.model.Category;
import com.gizasystems.inventoryservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductDao productDao;
    @Autowired
    CategoryDao categoryDao;

    // Retrieve a specific product by its id
    public Product getProductById(Integer id) {
        try{
            return productDao.findById(id).get();
        }catch (Exception e){
            throw new ApiRequestException("Couldn't retrieve product #"+id);
        }
    }

    // Retrieve the price of a specific item
    public BigDecimal getProductPrice(Integer id) {
        try{
            Optional<Product> product = productDao.findById(id);
            if(product.isPresent()){
                return (product.get()).getPrice();
            }
            throw new ApiRequestException("Couldn't find product #"+id);
        }catch (Exception e){
            throw new ApiRequestException("Couldn't get the price of product #"+id);
        }
    }

    public String getProductName(Integer id) {
        try{
            Optional<Product> product = productDao.findById(id);
            if(product.isPresent()){
                return (product.get()).getName();
            }
            throw new ApiRequestException("Couldn't find product #"+id);
        }catch (Exception e){
            throw new ApiRequestException("Couldn't get the name of product #"+id);
        }
    }

    // Add a new product
    public Product addProduct(ProductDto productDto) {
        Product product = new Product();
        try{
            Optional<Category> category = categoryDao.findById(productDto.getCategoryId());
            Category categoryFound;
            if (category.isPresent())
                categoryFound = category.get();
            else
                throw new ApiRequestException("Category #"+productDto.getCategoryId()+" doesn't exist");
            List<Integer>productsId = categoryFound.getProductsId();
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategoryId(productDto.getCategoryId());
            product.setPrice(productDto.getPrice());
            product.setQuantity(productDto.getQuantity());
            product.setImageUrl(productDto.getImageUrl());
            productDao.save(product);
            productsId.add(product.getId());
            categoryFound.setProductsId(productsId);
            categoryDao.save(categoryFound);
            return product;
        }catch (Exception e){
            throw new ApiRequestException("Couldn't add product "+productDto.getName());
        }
    }

    // Edit an already existing product
    public Product editProductById(Integer id, ProductDto productDto) {
        try {
            Optional<Product> product = productDao.findById(id);
            if(product.isPresent()){
                Product editedProduct = product.get();
                if (productDto.getName()!=null)
                  editedProduct.setName(productDto.getName());
                if (productDto.getDescription()!=null)
                 editedProduct.setDescription(productDto.getDescription());
                if(productDto.getCategoryId()!=null && categoryDao.findById(productDto.getCategoryId()).isPresent()) {
                    Category oldCategory = categoryDao.findById(editedProduct.getCategoryId()).get();
                    oldCategory.getProductsId().remove(editedProduct.getId());
                    categoryDao.save(oldCategory);
                    editedProduct.setCategoryId(productDto.getCategoryId());
                    Category newCategory = categoryDao.findById(productDto.getCategoryId()).get();
                    categoryDao.save(newCategory);
                }
                else
                    throw new ApiRequestException("Category #"+productDto.getCategoryId()+" doesn't exist");
                if(productDto.getPrice()!=null)
                    editedProduct.setPrice(productDto.getPrice());
                if(productDto.getQuantity()!=null)
                    editedProduct.setQuantity(productDto.getQuantity());
                if(productDto.getImageUrl()!=null)
                    editedProduct.setImageUrl(productDto.getImageUrl());
                return productDao.save(editedProduct);
            }
            throw new ApiRequestException("Couldn't find a product with id:"+id);
        }catch (Exception e){
            throw new ApiRequestException("Couldn't edit product #"+id);
        }
    }

    // Delete products by id
    public void deleteProductById(Integer id) {
        try{
            Optional<Product> product = productDao.findById(id);
            if(product.isPresent()){
               productDao.deleteById(id);
            }
            throw new ApiRequestException("Couldn't find a product with id:"+id);
        }catch (Exception e){
            System.out.println("Couldn't delete product #"+id);
        }
    }
}
