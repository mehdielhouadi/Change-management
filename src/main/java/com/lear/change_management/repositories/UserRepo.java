package com.lear.change_management.repositories;

import com.lear.change_management.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
