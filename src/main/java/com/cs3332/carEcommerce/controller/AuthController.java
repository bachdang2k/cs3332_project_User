package com.cs3332.carEcommerce.controller;

import com.cs3332.carEcommerce.DTOmodel.request.SignInForm;
import com.cs3332.carEcommerce.DTOmodel.request.SignUpForm;
import com.cs3332.carEcommerce.DTOmodel.response.JwtResponse;
import com.cs3332.carEcommerce.entity.Role;
import com.cs3332.carEcommerce.entity.RoleName;
import com.cs3332.carEcommerce.entity.User;
import com.cs3332.carEcommerce.DTOmodel.response.ResponseObject;
import com.cs3332.carEcommerce.DTOmodel.DTO.UserDTO;
import com.cs3332.carEcommerce.DTOmodel.mapper.UserMapper;
import com.cs3332.carEcommerce.security.Jwt.JwtProvider;
import com.cs3332.carEcommerce.security.userPrincipal.UserPrincipal;
import com.cs3332.carEcommerce.services.impl.RoleServiceImpl;
import com.cs3332.carEcommerce.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping(value = "/sign-up", consumes = {"application/xml","application/json"})
    public ResponseEntity<ResponseObject> register(@RequestBody SignUpForm signUpForm) {

        User newUser = new User();

        if (userService.existsByEmail(signUpForm.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", "User's email already taken", ""));
        } else {

            newUser = new User(signUpForm.getFirstName(), signUpForm.getLastName(), signUpForm.getEmail(), signUpForm.getPhone(), passwordEncoder.encode(signUpForm.getPassword()));

            Set<String> strRoles = signUpForm.getRoles();
            Set<Role> roles =  new HashSet<>();
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleService.findByRoleName(RoleName.ADMIN).orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(adminRole);
                        break;

                    case "author":
                        Role authorRole = roleService.findByRoleName(RoleName.AUTHOR).orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(authorRole);
                        break;

                    default:
                        Role userRole = roleService.findByRoleName(RoleName.USER).orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(userRole);
                }
            });

            newUser.setRoles(roles);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Register Successfully", userService.saveUser(newUser)));
        }
    }

//    @PostMapping("/sign-in") //security login API
//    public ResponseEntity<JwtResponse> login(@RequestBody SignInForm signInForm) throws Exception {
//
//        Authentication authentication;
//
//        UserDTO userDTO = new UserDTO();
//        String token;
//        //UserPrincipal userPrincipal;
//
//        try {
//
//            authentication = authenticationManager.authenticate( //Authentication: Interface
//                    new UsernamePasswordAuthenticationToken(signInForm.getEmail(), signInForm.getPassword())); //design a simple presentation of a username and password for Authentication Object.
//
//            SecurityContextHolder.getContext().setAuthentication(authentication); //set the resulting Authentication object into the current Security Context.
//            token = jwtProvider.createToken(authentication);
//            //userPrincipal = (UserPrincipal) authentication.getPrincipal();
//
//            userDTO = UserMapper.toUserDTO(userService.getUserByEmail(signInForm.getEmail()).get());
//
//        } catch (BadCredentialsException e) { //throw if an authentication request is rejected
//            throw new Exception("Invalid credentials");
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new JwtResponse("Login Successfully", token, userDTO));
//    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@Valid @RequestBody SignInForm signInForm) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getEmail(), signInForm.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.createToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse("Login successfully", token , userPrincipal.getFirstName(), userPrincipal.getAuthorities()));
    }

}
