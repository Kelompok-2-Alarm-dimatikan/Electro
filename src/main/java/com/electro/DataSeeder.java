package com.electro;

import com.electro.model.*;
import com.electro.repository.ElectroRepository;
import com.electro.repository.UserRepository;
import com.electro.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initData(ElectroRepository electroRepo,
                               UserRepository userRepo,
                               UserService userService) {
        return args -> {
            // Seed devices
            if (electroRepo.count() == 0) {
                electroRepo.save(new Hp("Samsung S23 Ultra", 14999000));
                electroRepo.save(new Hp("Samsung J2 Prime", 1499000));
                electroRepo.save(new Hp("Xiaomi 13", 8999000));
                electroRepo.save(new Hp("iPhone 14", 13999000));
                electroRepo.save(new Laptop("Asus ROG", 25999000));
                electroRepo.save(new Laptop("Acer Predator", 22999000));
                electroRepo.save(new Laptop("Lenovo Legion", 19999000));
                electroRepo.save(new Tablet("iPad Pro", 16999000));
                electroRepo.save(new Tablet("Samsung Galaxy Tab", 9999000));
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