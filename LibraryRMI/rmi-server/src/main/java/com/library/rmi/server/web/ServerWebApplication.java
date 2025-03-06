package com.library.rmi.server.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.library.rmi")
public class ServerWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerWebApplication.class, args);
    }
}
