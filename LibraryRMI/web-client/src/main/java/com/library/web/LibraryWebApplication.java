package com.library.web;

// Application imports
import com.library.rmi.service.LibraryService;

// Java imports
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// Spring imports
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class LibraryWebApplication {

    @Value("${rmi.registry.host}")
    private String rmiHost;

    @Value("${rmi.registry.port}")
    private int rmiPort;

    private String determineHost(String configuredHost) {
        if (configuredHost == null || configuredHost.contains("${")) {
            return "auto";
        }
        
        if (!"auto".equals(configuredHost)) {
            return configuredHost;
        }

        try {
            String hostname = InetAddress.getLocalHost().getHostAddress();
            if (hostname.equals("127.0.0.1") || hostname.equals("0:0:0:0:0:0:0:1")) {
                return "localhost";
            }
            return hostname;
        } catch (UnknownHostException e) {
            System.out.println("Impossibile determinare l'hostname, uso localhost: " + e.getMessage());
            return "localhost";
        }
    }

    @Bean
    public LibraryService libraryService() throws Exception {
        String configuredHost = System.getProperty("RMI_REGISTRY_HOST", rmiHost);
        String effectiveHost = determineHost(configuredHost);
        System.out.println("Connessione al registry RMI su: " + effectiveHost + ":" + rmiPort);
        System.setProperty("java.rmi.server.hostname", effectiveHost);
        Registry registry = LocateRegistry.getRegistry(effectiveHost, rmiPort);
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
