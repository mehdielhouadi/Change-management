package com.lear.change_management.security;


import com.lear.change_management.entities.User;
import com.lear.change_management.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;


@RequiredArgsConstructor
public class SecurityUser implements UserDetails {

    private final User user;
    @Autowired
    private UserRepo uRepo;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SecurityRole(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }
}
