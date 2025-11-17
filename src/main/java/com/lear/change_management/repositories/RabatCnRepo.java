package com.lear.change_management.repositories;

import com.lear.change_management.entities.Project;
import com.lear.change_management.entities.RabatCn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RabatCnRepo extends JpaRepository<RabatCn, Long> {

    @Query("SELECT rcn FROM RabatCn rcn WHERE rcn.project = ?1")
    List<RabatCn> findRcnsOfProject(Project project);

    List<RabatCn> findByProjectAndCreationDateBetween(Project project, LocalDate start, LocalDate end);
}
