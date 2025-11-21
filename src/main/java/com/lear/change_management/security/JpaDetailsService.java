package com.lear.change_management.security;

import com.lear.change_management.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JpaDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo uRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return uRepo.findByUserName(username).map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("no such user"));
    }
}
