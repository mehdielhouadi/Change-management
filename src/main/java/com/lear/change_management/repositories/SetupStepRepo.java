package com.lear.change_management.repositories;

import com.lear.change_management.entities.SetupStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SetupStepRepo extends JpaRepository<SetupStep, Long> {
    List<SetupStep> findByProject_Id(Long id);

    @Query("SELECT s FROM SetupStep s LEFT JOIN FETCH s.owners WHERE s.project.id = :projectId")
    List<SetupStep> findByProjectIdWithOwners(@Param("projectId") Long projectId);
}
