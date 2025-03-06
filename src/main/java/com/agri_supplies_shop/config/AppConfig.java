package com.agri_supplies_shop.config;

import com.agri_supplies_shop.entity.Users;
import com.agri_supplies_shop.enums.PredefinedRole;
import com.agri_supplies_shop.repository.RoleRepository;
import com.agri_supplies_shop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.ZonedDateTime;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByUserName("admin").isEmpty()) {
                Users user = Users.builder()
                        .userName("admin")
                        .password(passwordEncoder().encode("admin"))
                        .role(roleRepository.findByName(PredefinedRole.ADMIN_ROLE.getName()))
                        .createdAt(ZonedDateTime.now())
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
        };
    }

    //Enabling Cross Origin Requests
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Apply CORS policy to all endpoints
                        .allowedOrigins("http://localhost:3000") // Allowed origins
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // HTTP methods
                        .allowedHeaders("Authorization", "Content-Type") // Allowed headers
                        .allowCredentials(true); // Allow credentials (cookies, Authorization headers, etc.)
            }
        };
    }
}
