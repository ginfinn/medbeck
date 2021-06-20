package com.realityflex.medback;

import com.realityflex.medback.entity.Role;
import com.realityflex.medback.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MedbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedbackApplication.class, args);
    }


    @Bean
    public CommandLineRunner loadData(RoleRepository repository) {
        return (args) -> {
            repository.save(new Role("1", "ROLE_DOCTOR"));
            repository.save(new Role("2", "ROLE_PATIENT"));

        };
    }
}