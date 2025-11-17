package com.lear.change_management.services;

import com.lear.change_management.entities.Project;
import com.lear.change_management.entities.RabatCn;
import com.lear.change_management.repositories.RabatCnRepo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RabatCnService {

    @Autowired
    private RabatCnRepo rabatCnRepo;

    public List<RabatCn> getRcnsOfProject(Project project) {
        return rabatCnRepo.findRcnsOfProject(project);
    }
    public List<RabatCn> getRcnsOfProjectForYear(Project project, int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        return rabatCnRepo.findByProjectAndCreationDateBetween(project, start, end);
    }
}
