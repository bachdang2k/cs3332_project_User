package com.cs3332.carEcommerce.security.userPrincipal;

import com.cs3332.carEcommerce.entity.User;
import com.cs3332.carEcommerce.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found -> user name or password " + email));
        return UserPrincipal.build(user);
    }
}
