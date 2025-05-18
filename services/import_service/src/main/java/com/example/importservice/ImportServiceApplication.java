package com.example.importservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients; // Đảm bảo import này có

@SpringBootApplication
@EnableFeignClients // Quan trọng: Kích hoạt Feign Clients
public class ImportServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImportServiceApplication.class, args);
    }
}