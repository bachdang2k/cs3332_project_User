package com.cs3332.carEcommerce.services.impl;

import com.cs3332.carEcommerce.entity.User;
import com.cs3332.carEcommerce.model.API.UserModel;
import com.cs3332.carEcommerce.model.DTO.UserDTO;
import com.cs3332.carEcommerce.model.mapper.UserMapper;
import com.cs3332.carEcommerce.repositories.UserRepository;
import com.cs3332.carEcommerce.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getAllUser() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(Long id) {

        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUserByID(User newUser, Long id) {

        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    user.setEmail(newUser.getEmail());
                    user.setPhone(newUser.getPhone());
                    user.setAvatar(newUser.getAvatar());
                    return userRepository.save(newUser);
                }).orElseGet(() -> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });
    }

    @Override
    public User updateUserByEmail(User newUser, String email) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    user.setPhone(newUser.getPhone());
                    user.setAvatar(newUser.getAvatar());
                    return userRepository.save(newUser);
                }).orElseGet(() -> {
                    newUser.setEmail(email);
                    return userRepository.save(newUser);
                });
    }

    @Override
    public Boolean existingEmail(UserModel user) {
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        if (foundUser.isPresent())
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

}