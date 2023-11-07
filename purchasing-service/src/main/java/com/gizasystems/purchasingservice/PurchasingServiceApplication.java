package com.gizasystems.purchasingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PurchasingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurchasingServiceApplication.class, args);
	}

}
