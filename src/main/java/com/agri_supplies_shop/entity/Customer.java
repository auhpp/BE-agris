package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.Gender;
import com.agri_supplies_shop.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String fullName;

    private String avatar;

    private String phoneNumber;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @CreatedDate
    private ZonedDateTime createdAt;

    //Relationship
    //Address
    @OneToMany(mappedBy = "customer")
    private List<Address> addresses;

    //cart item
    @OneToMany(mappedBy = "customer")
    private List<Cart> carts;

    //Orders detail
    @OneToMany(mappedBy = "customer")
    private List<Orders> orders;

    //account
    @OneToOne
    private Account account;
}
