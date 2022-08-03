package com.cs3332.carEcommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "first_name", nullable = false, length = 25)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(nullable = false, unique = true, length = 45)
    private String phone;

    @JsonIgnore //when return request by json file, the file will hide password field
    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 100)
    private String avatar;

    @OneToOne
    @JoinColumn(name = "Authority_id")
    private Authority authority;

}
