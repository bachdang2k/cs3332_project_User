package com.cs3332.carEcommerce.model.DTO;

import lombok.Data;

@Data
public class UserDTO {

    private Long Id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String avatar;
    //private String role;

}
