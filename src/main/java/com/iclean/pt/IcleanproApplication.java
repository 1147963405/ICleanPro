package com.iclean.pt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = "classpath:spring/*.xml")
public class IcleanproApplication {
    public static void main(String[] args) {
        SpringApplication.run(IcleanproApplication.class, args);
    }
}
