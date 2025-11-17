package com.lear.change_management.repositories;

import com.lear.change_management.entities.Project;
import com.lear.change_management.entities.RabatCn;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RabatCnRepo extends JpaRepository<RabatCn, Long> {

    @Query("SELECT rcn FROM RabatCn rcn WHERE rcn.project = ?1")
    List<RabatCn> findRcnsOfProject(Project project);

    List<RabatCn> findByProjectAndCreationDateBetween(Project project, LocalDate start, LocalDate end);


    @Query("""
            SELECT rcn FROM RabatCn rcn LEFT JOIN FETCH rcn.changeNotices cn WHERE lower(concat('%',rcn.name,'%')) like lower(concat('%', :filterText, '%'))
            """)
    List<RabatCn> searchAllWithCns(@Param("filterText") String filterText);

}
