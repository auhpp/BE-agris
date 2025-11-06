package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.Status;
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

    @Enumerated(EnumType.STRING)
    private Status status;

    private ZonedDateTime createdAt;
    //Relationship
    //role
    @ManyToOne
    private Role role;
}
