package com.lear.change_management.services;

import com.lear.change_management.entities.Project;
import com.lear.change_management.entities.RabatCn;
import com.lear.change_management.repositories.ProjectRepo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    public Project addProject(Project p) {
        return projectRepo.save(p);
    }

    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepo.findById(id).orElse(null);
    }

    public Project updateProject(Project p, Set<RabatCn> rcnList, String name) {
        p.setName(name);

        // affect the project to every rcn for backward relationship
        rcnList.forEach(rcn -> rcn.setProject(p));

        p.setRabatCns(rcnList);
        return p;
    }

    public void deleteProject(Long id) {
        projectRepo.deleteById(id);
    }

    public List<Project> getAllProjectsWithRcns(String filterText) {
        // Fetch all projects with RCNs eagerly using a join fetch
        return projectRepo.findAllWithRcns();
    }

    public List<Project> getAllProjectsWithRcnsForYear(int year, String filterText) {
        if (null == filterText || filterText.isEmpty()) {
            return projectRepo.findAllWithRcns();
        }

        List<Project> projects = projectRepo.searchAllWithRcns(filterText);
        for (Project p : projects) {
            p.setRabatCns(
                    p.getRabatCns().stream()
                            .filter(r -> r.getCreationDate().getYear() == year)
                            .collect(Collectors.toSet()));
        }
        return projects;
    }




}
