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
        urlRoles = new HashMap<String, String>();
        populateUrls();
    }

    private void populateUrls() {
        urlRoles.put("/user/add-admin", "MANAGER");
        urlRoles.put("/user/delete-admin", "MANAGER");
        urlRoles.put("/purchase/get-purchase", "CUSTOMER-ADMIN-MANAGER");
    }



    public boolean isUserAuthorized(String url, String role){
        boolean isAuthorized = false;
        for (Map.Entry<String, String> entry : urlRoles.entrySet()) {
            if(entry.getKey().startsWith(url)){
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


}
