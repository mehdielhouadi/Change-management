package com.lear.change_management;

import com.lear.change_management.entities.Role;
import com.lear.change_management.entities.User;
import com.lear.change_management.repositories.RoleRepo;
import com.lear.change_management.repositories.UserRepo;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AdminAdder implements CommandLineRunner {
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final RoleRepo roleRepo;
    @Override
    public void run(String... args) throws Exception {

        Role role = new Role();
        role.setName("ROLE_ADMIN");
        roleRepo.save(role);
        User admin = new User();
        admin.setUserName("admin");
        admin.setPassword("admin");
        admin.setRole(role);
        userRepo.save(admin);


        Role role2 = new Role();
        role2.setName("ROLE_ENGINEERING");
        roleRepo.save(role2);
        User engineer = new User();
        engineer.setUserName("eng");
        engineer.setPassword("eng");
        engineer.setRole(role2);
        userRepo.save(engineer);
    }
}
