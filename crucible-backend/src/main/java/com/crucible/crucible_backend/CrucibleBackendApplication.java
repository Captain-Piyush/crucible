package com.crucible.crucible_backend;

import com.crucible.crucible_backend.entity.User;
import com.crucible.crucible_backend.repository.UserRepository;
import com.crucible.escrow.service.EscrowService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// CRITICAL FIX: This tells Spring to scan your new 'escrow' folders too!
@SpringBootApplication(scanBasePackages = {"com.crucible"})
public class CrucibleBackendApplication {

    public static void main(String[] args) {
        // CRITICAL FIX: Force Java to use the modern timezone name PostgreSQL expects
        java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("Asia/Kolkata"));

        SpringApplication.run(CrucibleBackendApplication.class, args);
    }

    // This runs automatically exactly once when the application boots
    @Bean
    CommandLineRunner initApp(UserRepository userRepository, EscrowService escrowService) {
        return args -> {
            System.out.println("====== SPRING BOOT STARTING ======");

            // 1. Initialize the Database
            if (userRepository.count() == 0) {
                User testUser = new User("Test Poster", "poster@test.com", "POSTER");
                userRepository.save(testUser);
            }
            User firstUser = userRepository.findAll().get(0);
            System.out.println("--- DB READY: USE POSTER ID " + firstUser.getId() + " IN POSTMAN ---");

            // 2. Initialize the Blockchain Deployment
            System.out.println("--- BLOCKCHAIN READY: Attempting to connect to Hardhat Node... ---");
           // escrowService.deployEscrowFactory();

            System.out.println("==================================");
        };
    }
}
