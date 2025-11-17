package com.lear.change_management.repositories;

import com.lear.change_management.entities.ChangeNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeNoticeRepo extends JpaRepository<ChangeNotice, Long> {
}
