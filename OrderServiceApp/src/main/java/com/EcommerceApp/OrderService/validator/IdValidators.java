package com.EcommerceApp.OrderService.validator;

import com.EcommerceApp.OrderService.exception.InvalidCustomerIdException;
import com.EcommerceApp.OrderService.exception.InvalidOrderIdException;

public class IdValidators {

    public void validateOrderId(Integer orderId){
        if(orderId <= 0) throw new InvalidOrderIdException("Order id is Invalid");
    }

    public void validateCustomerId(Integer customerId){
        if(customerId <= 0) throw new InvalidCustomerIdException("Customer id is Invalid");
    }

}
