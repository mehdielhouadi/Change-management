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
        if (roleRepo.count() == 0){
            role.setName("ROLE_ADMIN");
            role = roleRepo.save(role);
        }  else {
            role = roleRepo.findByName("ROLE_ADMIN");
        }

        if (userRepo.count() == 0) {
            User admin = new User();
            admin.setEmail("admin@lear.com");
            admin.setUserName("admin");
            admin.setPassword("admin");
            admin.setRole(role);
            userRepo.save(admin);
        }
        Role role1 = new Role();
        if (roleRepo.count() == 1){
            role1.setName("ROLE_ENGINEER");
            role1 = roleRepo.save(role1);
        }

    }
}
