package com.example.rentacareurekaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RentacarEurekaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentacarEurekaServiceApplication.class, args);
	}

}
