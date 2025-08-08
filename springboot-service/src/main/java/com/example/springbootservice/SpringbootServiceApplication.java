package com.example.springbootservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringbootServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootServiceApplication.class, args);
    }

}
