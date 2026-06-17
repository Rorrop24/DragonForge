package com.DragonForge.wiki_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WikiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WikiServiceApplication.class, args);
	}

}
