package com.cs3332.carEcommerce.DTOmodel.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtResponse {

    private String message;
    private String token;
    private String type = "Bearer";
    private String name;
    private Collection<? extends GrantedAuthority> roles;

    private Object data;

    public JwtResponse(String message, String token, String name, Collection<? extends GrantedAuthority> roles) {
        this.message = message;
        this.token = token;
        this.name = name;
        this.roles = roles;
    }


//    public JwtResponse(String message, String token, Object data) {
//        this.message = message;
//        this.token = token;
//        this.data = data;
//    }
//
//    @Override
//    public String toString() {
//        return "ResponseJwt{" +
//                ", massage='" + message + '\'' +
//                ", token='" + token + '\'' +
//                ", type='" + type + '\'' +
//                ", data=" + data +
//                '}';
//    }

}
