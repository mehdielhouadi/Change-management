package com.lear.change_management.services;

import com.lear.change_management.entities.User;
import com.lear.change_management.repositories.UserRepo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public List<User> getAll() {
        return userRepo.findAll();
    }


}
