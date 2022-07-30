package com.cs3332.carEcommerce.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_authorities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;
}
