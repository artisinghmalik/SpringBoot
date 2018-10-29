package com.example.restApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.example.restApi")
public class RESTApiMain {

	public static void main(String[] args) {
		SpringApplication.run(RESTApiMain.class, args);
	}

}
