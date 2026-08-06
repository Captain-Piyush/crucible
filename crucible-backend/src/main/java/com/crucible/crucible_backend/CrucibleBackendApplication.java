package com.crucible.crucible_backend;

import com.crucible.crucible_backend.entity.User;
import com.crucible.crucible_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CrucibleBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrucibleBackendApplication.class, args);
    }

    // This runs automatically exactly once when the application boots
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // 1. Create the user if the table is completely empty
            if (userRepository.count() == 0) {
                User testUser = new User("Test Poster", "poster@test.com", "POSTER");
                userRepository.save(testUser);
            }

            // 2. Fetch the very first user in the database and print their actual ID
            User firstUser = userRepository.findAll().get(0);
            System.out.println("=====================================================");
            System.out.println("--- USE THIS POSTER ID IN YOUR POST REQUEST: " + firstUser.getId() + " ---");
            System.out.println("=====================================================");
        };
    }
}