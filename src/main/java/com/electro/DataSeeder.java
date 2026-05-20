package com.electro;

import com.electro.model.*;
import com.electro.repository.DeviceRepository;
import com.electro.repository.UserRepository;
import com.electro.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initData(DeviceRepository deviceRepo,
                               UserRepository userRepo,
                               UserService userService) {
        return args -> {
            // Seed devices
            if (deviceRepo.count() == 0) {
                deviceRepo.save(new Hp("Samsung S23 Ultra", 14999000, 10, "Samsung"));
                deviceRepo.save(new Hp("Samsung J2 Prime", 1499000, 10, "Samsung"));
                deviceRepo.save(new Hp("Xiaomi 13", 8999000, 10, "Xiaomi"));
                deviceRepo.save(new Hp("iPhone 14", 13999000, 10, "Apple"));
                deviceRepo.save(new Laptop("Asus ROG", 25999000, 10, "Asus"));
                deviceRepo.save(new Laptop("Acer Predator", 22999000, 10, "Acer"));
                deviceRepo.save(new Laptop("Lenovo Legion", 19999000, 10, "Lenovo"));
                deviceRepo.save(new Tablet("iPad Pro", 16999000, 10, "Apple"));
                deviceRepo.save(new Tablet("Samsung Galaxy Tab", 9999000, 10, "Samsung"));
            }

            // Seed user admin
            if (userRepo.count() == 0) {
                userService.register("admin", "admin123");
                // Set role admin
                var admin = userRepo.findByUsername("admin").get();
                admin.setRole("ROLE_ADMIN");
                userRepo.save(admin);
            }
        };
    }
}