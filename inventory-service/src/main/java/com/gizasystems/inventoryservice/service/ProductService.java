package com.gizasystems.inventoryservice.service;

import com.gizasystems.inventoryservice.dao.CategoryDao;
import com.gizasystems.inventoryservice.dao.ProductDao;
import com.gizasystems.inventoryservice.dto.ProductDto;
import com.gizasystems.inventoryservice.exception.CategoryNotFoundException;
import com.gizasystems.inventoryservice.exception.ProductNotFoundException;
import com.gizasystems.inventoryservice.mapper.ProductMapper;
import com.gizasystems.inventoryservice.model.Category;
import com.gizasystems.inventoryservice.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    ProductDao productDao;
    CategoryDao categoryDao;
    ProductMapper productMapper;

    public ProductService(ProductDao productDao, CategoryDao categoryDao, ProductMapper productMapper) {
        this.productDao = productDao;
        this.categoryDao = categoryDao;
        this.productMapper = productMapper;
    }

    // Retrieve a specific product by its id
    public ProductDto getProductById(Integer id) {
        Optional<Product> product = productDao.findById(id);
        if(product.isEmpty())
            throw new ProductNotFoundException("No product with id #" + id);

        return productMapper.transferToDto(productDao.findById(id).orElse(null));
    }

    // Retrieve the price of a specific item
    public BigDecimal getProductPrice(Integer id) {
        Optional<Product> product = productDao.findById(id);
        if(product.isEmpty())
            throw new ProductNotFoundException("Couldn't find product #"+id);

        return product.get().getPrice();
    }

    public String getProductName(Integer id) {
        Optional<Product> product = productDao.findById(id);
        if(product.isEmpty())
            throw new ProductNotFoundException("Couldn't find product #"+id);

        return product.get().getName();
    }

    // Add a new product
    public ProductDto addProduct(ProductDto productDto) {
        Optional<Category> category = categoryDao.findById(productDto.categoryId());
        if (category.isEmpty())
            throw new CategoryNotFoundException("Couldn't find a category with id #"+productDto.categoryId());

        Product product = productMapper.transferToEntity(productDto);
        productDao.save(product);

        Category categoryFound = category.get();
        List<Integer>productsId = categoryFound.getProductsId();
        productsId.add(product.getId());
        categoryFound.setProductsId(productsId);
        categoryDao.save(categoryFound);

        return productMapper.transferToDto(product);
    }

    // Edit an already existing product
    public ProductDto editProductById(Integer id, ProductDto productDto) {
        Optional<Product> product = productDao.findById(id);
        if(product.isEmpty())
            throw new ProductNotFoundException("Couldn't find a product with id #"+id);

        Product editedProduct = product.get();
        if (productDto.name()!=null)
          editedProduct.setName(productDto.name());
        if (productDto.description()!=null)
         editedProduct.setDescription(productDto.description());

        if(productDto.categoryId()!=null) {
            Optional<Category> category = categoryDao.findById(productDto.categoryId());
            if(category.isEmpty())
                throw new CategoryNotFoundException("Couldn't find a category with id #"+productDto.categoryId());


            Category newCategory = category.get();
            newCategory.getProductsId().add(editedProduct.getId());
            categoryDao.save(newCategory);

            Category oldCategory = categoryDao.findById(editedProduct.getCategoryId()).orElse(null);
            assert oldCategory != null;

            oldCategory.getProductsId().remove(editedProduct.getId());
            categoryDao.save(oldCategory);

            editedProduct.setCategoryId(productDto.categoryId());
        }

        if(productDto.price()!=null)
            editedProduct.setPrice(productDto.price());
        if(productDto.quantity()!=null)
            editedProduct.setQuantity(productDto.quantity());
        if(productDto.imageUrl()!=null)
            editedProduct.setImageUrl(productDto.imageUrl());

        productDao.save(editedProduct);
        return productMapper.transferToDto(editedProduct);
    }

    // Delete products by id
    public void deleteProductById(Integer id) {
        Optional<Product> product = productDao.findById(id);
        if(product.isEmpty())
            throw new ProductNotFoundException("Couldn't find a product with id:"+id);

        Optional<Category> category = categoryDao.findById(product.get().getCategoryId());
        category.ifPresent(value -> value.getProductsId().remove(product.get().getId()));
        productDao.deleteById(id);
    }
}
