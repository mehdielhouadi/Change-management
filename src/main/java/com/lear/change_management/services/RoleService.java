package com.lear.change_management.services;

import com.lear.change_management.entities.Role;
import com.lear.change_management.repositories.RoleRepo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public List<Role> getAll() {
        return roleRepo.findAll();
    }
}
