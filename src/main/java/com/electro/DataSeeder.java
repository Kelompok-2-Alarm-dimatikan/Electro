package com.electro;

import com.electro.model.*;
import com.electro.repository.DeviceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initData(DeviceRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Hp("Samsung S23 Ultra", 10000));
                repo.save(new Hp("Samsung J2 Prime", 10000));
                repo.save(new Hp("Xiaomi 13", 10000));
                repo.save(new Hp("iPhone 14", 10000));
                repo.save(new Laptop("Asus ROG", 30000));
                repo.save(new Laptop("Acer Predator", 25000));
                repo.save(new Laptop("Lenovo Legion", 35000));
                repo.save(new Tablet("iPad Pro", 15000));
                repo.save(new Tablet("Samsung Galaxy Tab", 12000));
            }
        };
    }
}