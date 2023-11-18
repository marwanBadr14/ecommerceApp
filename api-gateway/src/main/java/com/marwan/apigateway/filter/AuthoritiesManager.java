package com.marwan.apigateway.filter;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class AuthoritiesManager {

    // Add list of all URLs in the system with their required authorities

    private HashMap<String, String> urlRoles;

    public AuthoritiesManager(){
        urlRoles = new HashMap<String, String>();
        populateUrls();
    }


    private void populateUrls() {
        urlRoles.put("/user/add-admin", "MANAGER");
        urlRoles.put("/purchase/get-purchase", "CUSTOMER-ADMIN-MANAGER");
        urlRoles.put("/categories/all", "CUSTOMER-ADMIN-MANAGER");
        urlRoles.put("/categories/{id}'", "CUSTOMER-ADMIN-MANAGER");
        urlRoles.put("/products/{id}", "CUSTOMER-ADMIN-MANAGER");
        urlRoles.put("/inventory/categories/{categoryName}", "CUSTOMER-ADMIN-MANAGER");
        urlRoles.put("/categories/add", "ADMIN-MANAGER");
        urlRoles.put("/categories/edit/{id}", "ADMIN-MANAGER");
        urlRoles.put("/categories/delete/{id}", "ADMIN-MANAGER");
        urlRoles.put("/products/add", "ADMIN-MANAGER");
        urlRoles.put("/products/edit/{id}", "ADMIN-MANAGER");
        urlRoles.put("/products/delete/{id}", "ADMIN-MANAGER");
        urlRoles.put("/inventory/all", "CUSTOMER-ADMIN-MANAGER");
    }



    public boolean isUserAuthorized(String url, String role){
        String[] s = urlRoles.get(url).split("-");
        List<String> roles = Arrays.asList(s);
        return (roles.contains(role));
    }


}
