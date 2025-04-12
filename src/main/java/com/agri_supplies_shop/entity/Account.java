package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String userName;

    private String password;

    private ZonedDateTime createdAt;
    //Relationship
    //role
    @ManyToOne
    private Role role;
}
