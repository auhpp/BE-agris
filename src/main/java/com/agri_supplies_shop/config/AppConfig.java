package com.agri_supplies_shop.config;

import com.agri_supplies_shop.entity.Account;
import com.agri_supplies_shop.entity.Staff;
import com.agri_supplies_shop.enums.PredefinedRole;
import com.agri_supplies_shop.repository.AccountRepository;
import com.agri_supplies_shop.repository.RoleRepository;
import com.agri_supplies_shop.repository.StaffRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.ZonedDateTime;
import java.util.Properties;

@Configuration
@Slf4j
public class AppConfig {

    @Value("${EMAIL_PASSWORD}")
    private String emailPassword;

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
    @Transactional
    ApplicationRunner applicationRunner(StaffRepository staffRepository,
                                        AccountRepository accountRepository,
                                        RoleRepository roleRepository) {
        return args -> {
            if (accountRepository.findByUserName("admin").isEmpty()) {
                Account admin = Account.builder()
                        .userName("admin")
                        .password(passwordEncoder().encode("admin"))
                        .role(roleRepository.findByName(PredefinedRole.ADMIN_ROLE.getName()))
                        .createdAt(ZonedDateTime.now())
                        .build();
                accountRepository.save(admin);
                Staff staff = Staff.builder()
                        .createdAt(ZonedDateTime.now())
                        .account(admin)
                        .build();
                staffRepository.save(staff);
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

    @Bean
    public JavaMailSender javaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("hpphiau@gmail.com");
        mailSender.setPassword(emailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }

}
