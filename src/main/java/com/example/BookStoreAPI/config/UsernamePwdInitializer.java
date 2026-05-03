package com.example.BookStoreAPI.config;

import com.example.BookStoreAPI.entity.AppUser;
import com.example.BookStoreAPI.repo.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsernamePwdInitializer {

    @Bean
    public CommandLineRunner loadUsers(AppUserRepository repo,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            if (repo.findByUsername("user").isEmpty()) {
                AppUser user = new AppUser();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole("USER");
                repo.save(user);
            }

            if (repo.findByUsername("admin").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                repo.save(admin);
            }
        };
    }

}
