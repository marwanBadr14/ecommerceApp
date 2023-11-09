package com.gizasystems.notificationservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("USER-SERVICE")
public interface UserServiceInterface {

    @GetMapping("/user/get-user-email-by-id")
    public String getUserEmailById(Integer id);


}
