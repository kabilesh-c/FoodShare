package com.example.foodshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.foodshare.repository")
public class FoodShareApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodShareApplication.class, args);
    }
}
