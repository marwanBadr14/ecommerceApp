package com.marwan.apigateway.filter;


import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class AuthoritiesManager {

    // Add list of all URLs in the system with their required authorities

    private HashMap<String, String> urlRoles;

    public AuthoritiesManager(){
        urlRoles = new HashMap<>();
        populateUrls();
    }


    private void populateUrls() {
        urlRoles.put("/user/add-admin", "MANAGER");
        urlRoles.put("/user/delete-admin", "MANAGER");
        urlRoles.put("/user/admins", "MANAGER");
        urlRoles.put("/user/all", "MANAGER");
        urlRoles.put("/user/{id}", "MANAGER");
        urlRoles.put("/purchase/get-purchase", "CUSTOMER-ADMIN-MANAGER");
        urlRoles.put("/categories/all", "CUSTOMER-ADMIN-MANAGER");
        urlRoles.put("/categories/{id}'", "CUSTOMER-ADMIN-MANAGER");
        urlRoles.put("/products/{id}", "CUSTOMER-ADMIN-MANAGER");
        urlRoles.put("/products", "CUSTOMER-ADMIN-MANAGER");
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
        boolean isAuthorized = false;
        for (Map.Entry<String, String> entry : urlRoles.entrySet()) {
            if(isTwoPathsEqual(entry.getKey(), url)){
                String[] s = entry.getValue().split("-");
                List<String> roles = Arrays.asList(s);
                if(roles.contains(role)){
                    isAuthorized = true;
                    break;
                }
            }
        }
        return isAuthorized;
    }

    public boolean isTwoPathsEqual(String path1, String path2) {
        String[] path1Parts = path1.split("/");
        String[] path2Parts = path2.split("/");
        if(path1Parts.length != path2Parts.length)
            return false;
        for(int i=0; i<path1Parts.length; i++){
            if(path1Parts[i].startsWith("{") && path1Parts[i].endsWith("}"))
                continue;
            if(!path1Parts[i].equals(path2Parts[i]))
                return false;
        }
        return true;
    }

}
