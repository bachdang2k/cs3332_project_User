package com.cs3332.carEcommerce.controller;

import com.cs3332.carEcommerce.entity.responseEntity.ResponseObject;
import com.cs3332.carEcommerce.entity.User;
import com.cs3332.carEcommerce.model.API.UserModel;
import com.cs3332.carEcommerce.model.DTO.UserDTO;
import com.cs3332.carEcommerce.model.mapper.UserMapper;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;



    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserDTO> getListUser() {
        return userService.getAllUser();
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@RequestBody UserModel userModel) {

        User newUser = new User();

        if (userService.existingEmail(userModel)) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", "User's email already taken", ""));
        } else {

            newUser.setEmail(userModel.getEmail());
            newUser.setPassword(passwordEncoder.encode(userModel.getPassword()));
            newUser.setFirstName(userModel.getFirstName());
            newUser.setLastName(userModel.getLastName());
            newUser.setPhone(userModel.getPhone());
            newUser.setAvatar(userModel.getAvatar());
            newUser.setAuthority(userModel.getAuthority());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Register Successfully", userService.saveUser(newUser)));
        }
    }

    @PostMapping("/login") //security login API
    public ResponseEntity<ResponseObject> login(@RequestBody UserModel userModel) throws Exception {
        //@RequestBody -> transform from json... -> Object in Java -> sent to UserModel
        //@ResponseBody -> convert from Java Object -> Json -> show in Views
        Authentication authentication;

        UserDTO userDTO = new UserDTO();

        try {

            authentication = authenticationManager.authenticate( //Authentication: Interface
                    new UsernamePasswordAuthenticationToken(userModel.getEmail(), userModel.getPassword())); //design a simple presentation of a username and password for Authentication Object.
            SecurityContextHolder.getContext().setAuthentication(authentication); //set the resulting Authentication object into the current Security Context.

            userDTO = UserMapper.toUserDTO(userService.getUserByEmail(userModel.getEmail()).get());

        } catch (BadCredentialsException e) { //throw if an authentication request is rejected
            throw new Exception("Invalid credentials");
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Login Successfully", userDTO));

    }

    //profile
    @GetMapping(path = "/users/{id}")
    public ResponseEntity<ResponseObject> getUserByID(@PathVariable Long id) {

        Optional<User> foundUser = userService.getUserById(id);

        if (foundUser.isPresent()) {

            UserDTO userDTO = UserMapper.toUserDTO(foundUser.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Query user successfully", userDTO)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed", "Cannot find user with id = " + id, "")
            );
        }

    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ResponseObject> updateUserByID(@RequestBody User newUser, @PathVariable Long id) {

        User updatedUser = userService.updateUserByID(newUser, id);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update User by ID Successful", updatedUser)
        );
    }

    //profile
    @PutMapping("/users/{email}")
    public ResponseEntity<ResponseObject> updateUserByEmail(@RequestBody User newUser, @PathVariable String email) {

        User updatedUser = userService.updateUserByEmail(newUser, email);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update User by Email Successful", updatedUser)
        );
    }

}
