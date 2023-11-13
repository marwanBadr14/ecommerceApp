package com.gizasystems.inventoryservice.advice;

import com.gizasystems.inventoryservice.exception.CategoryNotFoundException;
import com.gizasystems.inventoryservice.exception.ProductNotFoundException;
import com.gizasystems.inventoryservice.exception.QuantityExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = ProductNotFoundException.class)
    public Map<String,String> handleProductNotFoundException(ProductNotFoundException ex){
        Map<String,String> error = new HashMap<>();
        error.put("Product Error: ",ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = CategoryNotFoundException.class)
    public Map<String,String> handleCategoryNotFoundException(CategoryNotFoundException ex){
        Map<String,String> error = new HashMap<>();
        error.put("Category Error: ",ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = QuantityExceededException.class)
    public Map<String,String> handleQuantityExceededException(QuantityExceededException ex){
        Map<String,String> error = new HashMap<>();
        error.put("Quantity Exceeded Error: ",ex.getMessage());
        return error;
    }

}
