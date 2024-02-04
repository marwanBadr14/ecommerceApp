package com.gizasystems.notificationservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("USER-SERVICE")
public interface UserServiceInterface {

    @GetMapping("/user/get-user-email-by-id/{id}")
    public ResponseEntity<String> getUserEmailById(@PathVariable Integer id);


}