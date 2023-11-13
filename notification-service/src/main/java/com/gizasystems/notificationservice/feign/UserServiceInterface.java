package com.gizasystems.notificationservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("USER-SERVICE")
public interface UserServiceInterface {

    @GetMapping("/get-user-email-by-id")
    public ResponseEntity<String> getUserEmailById(@RequestParam("id") Integer id);


}
