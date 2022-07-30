package com.cs3332.carEcommerce.model.API;

import com.cs3332.carEcommerce.entity.Authority;
import lombok.Data;

@Data
public class UserModel {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String avatar;
    private Authority authority;

}
