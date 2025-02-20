package com.library.web;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.library.rmi.service.LibraryService;

@SpringBootApplication
public class LibraryWebApplication {

    @Value("${rmi.registry.host}")
    private String rmiHost;

    @Value("${rmi.registry.port}")
    private int rmiPort;

    @Bean
    public LibraryService libraryService() throws Exception {
        System.setProperty("java.rmi.server.hostname", rmiHost);
        Registry registry = LocateRegistry.getRegistry(rmiHost, rmiPort);
        return (LibraryService) registry.lookup("LibraryService");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(LibraryWebApplication.class, args);
    }
}
