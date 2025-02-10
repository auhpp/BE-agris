package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.Gender;
import com.agri_supplies_shop.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String fullName;

    @Column(nullable = false)
    private String userName;

    private String avatar;

    private String password;

    private String phoneNumber;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Date dateOfBirth;

    private String provider;

    @Column(nullable = false)
    @CreatedDate
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    //Relationship
    //Role
    @ManyToOne
    @JoinColumn(nullable = false)
    private Role role;

    //Address
    @OneToMany(mappedBy = "user")
    private List<Address> addresses;

    //cart item
    @OneToMany(mappedBy = "user")
    private List<CartItem> cartItems;

    //Order detail
    @OneToMany(mappedBy = "user")
    private List<OrderDetail> orderDetails;

    //review
    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
}
