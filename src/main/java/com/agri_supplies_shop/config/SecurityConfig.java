package com.agri_supplies_shop.config;

import com.agri_supplies_shop.enums.PredefinedRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] PUBLIC_POST_ENDPOINTS = {
            "/customer", "/auth/token", "/auth/introspect", "/auth/logout", "/auth/refresh",
            "/email/otp/**", "/account/password/forget"
    };
    private final String[] PUBLIC_GET_ENDPOINTS = {
            "/product/search", "/category", "/image/**", "/product/get/**", "/payments/IPN"
    };
    private final String[] PRIVATE_POST_ENDPOINTS = {
            "/product", "/category", "/staff/account"
    };
    private final String[] PRIVATE_GET_ENDPOINTS = {
            "/supplier", "/customer", "/role"
    };
    private final String[] PRIVATE_DELETE_ENDPOINTS = {
            "/product/**", "/customer", "/category", "/supplier", "/staff/recall"
    };
    @Autowired
    private CustomJwtDecoder jwtDecoder;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //Config api endpoint need authenticated and unauthenticated
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, PRIVATE_POST_ENDPOINTS).hasAuthority("SCOPE_" + PredefinedRole.ADMIN_ROLE.getName())
                        .requestMatchers(HttpMethod.GET, PRIVATE_GET_ENDPOINTS).hasAuthority("SCOPE_" + PredefinedRole.ADMIN_ROLE.getName())
                        .requestMatchers(HttpMethod.DELETE, PRIVATE_DELETE_ENDPOINTS).hasAuthority("SCOPE_" + PredefinedRole.ADMIN_ROLE.getName())
                        .anyRequest().authenticated()
        );
        //Config oauth2 for jwt to verify token on header
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder)
                        )
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())

        );
        //Disable csrf
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }


}
