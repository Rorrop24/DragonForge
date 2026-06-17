package com.DragonForge.rng_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RngServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RngServiceApplication.class, args);
	}

}
