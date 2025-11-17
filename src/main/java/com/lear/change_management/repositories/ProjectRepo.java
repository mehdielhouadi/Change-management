package com.lear.change_management.repositories;

import com.lear.change_management.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.rabatCns rcn LEFT JOIN FETCH rcn.changeNotices cn")
    List<Project> findAllWithRcns();

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.rabatCns rcn LEFT JOIN FETCH rcn.changeNotices cn WHERE p.name like :proj")
    List<Project> findProjectWithRcns(@Param("proj") String projectName);


    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.rabatCns r LEFT JOIN FETCH r.changeNotices cn WHERE lower(concat('%',p.name,'%')) like lower(concat('%', :filterText, '%')) ")
    List<Project> searchAllWithRcns(@Param("filterText") String filterText);

    boolean existsByNameIgnoreCase(String name);

}
