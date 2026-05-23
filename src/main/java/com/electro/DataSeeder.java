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
            if (electroRepo.count() == 0) {
                electroRepo.save(new Hp("Samsung S23 Ultra", 14999000, 10, "Samsung"));
                electroRepo.save(new Hp("Samsung J2 Prime", 1499000, 10, "Samsung"));
                electroRepo.save(new Hp("Xiaomi 13", 8999000, 10, "Xiaomi"));
                electroRepo.save(new Hp("iPhone 14", 13999000, 10, "Apple"));
                electroRepo.save(new Laptop("Asus ROG", 25999000, 10, "Asus"));
                electroRepo.save(new Laptop("Acer Predator", 22999000, 10, "Acer"));
                electroRepo.save(new Laptop("Lenovo Legion", 19999000, 10, "Lenovo"));
                electroRepo.save(new Tablet("iPad Pro", 16999000, 10, "Apple"));
                electroRepo.save(new Tablet("Samsung Galaxy Tab", 9999000, 10, "Samsung"));
            }

            if (userRepo.count() == 0) {
                userService.register("admin", "admin@electro.com", "admin123");
                var admin = userRepo.findByUsername("admin").get();
                admin.setRole("ADMIN");
                userRepo.save(admin);
            }
        };
    }
}
