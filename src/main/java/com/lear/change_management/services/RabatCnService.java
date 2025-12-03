package com.lear.change_management.services;

import com.lear.change_management.entities.ChangeNotice;
import com.lear.change_management.entities.Project;
import com.lear.change_management.entities.RabatCn;
import com.lear.change_management.repositories.ChangeNoticeRepo;
import com.lear.change_management.repositories.ProjectRepo;
import com.lear.change_management.repositories.RabatCnRepo;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Service
public class RabatCnService {

    @Autowired
    private RabatCnRepo rabatCnRepo;
    @Autowired
    private ChangeNoticeRepo cnRepo;
    @Autowired
    private ProjectRepo pRepo;

    public void addRcn(RabatCn rabatCn) {
        rabatCnRepo.save(rabatCn);
    }


    public Long getCount() {
       return rabatCnRepo.count();
    }

    @Transactional
    public void deleteRcn(RabatCn rcn) {
        Project project = pRepo.findById(rcn.getProject().getId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // remove rcn from all the cns that contain it
        for (ChangeNotice cn : getAllCnsOfRcn(rcn)) {
            if (cn!=null) {
                cn.getRabatCns().remove(rcn);
                break;
            }
        }
        rcn.setChangeNotices(Set.of());
        rcn.getAffectedVariants().clear();
        project.getRabatCns().remove(rcn);

        // p.save after removing from list bc orphan removal
        pRepo.save(project);

    }

    public List<RabatCn> getRcnsOfProject(Project project) {
        return rabatCnRepo.findRcnsOfProject(project);
    }
    public List<RabatCn> getRcnsOfProjectForYear(Project project, int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        return rabatCnRepo.findByProjectAndCreationDateBetween(project, start, end);
    }

    public List<RabatCn> getAllRcns() {
        return rabatCnRepo.findAll();
    }
    public List<RabatCn> getAllRcnsOfProject(Project project) {
        return rabatCnRepo.findAllByProjectName(project.getName());
    }

    public List<RabatCn> getAllRcns(String filterText) {
        if (null==filterText || filterText.isEmpty()) {
            return rabatCnRepo.findAll();
        }
        else {
            return rabatCnRepo.searchAllWithCns(filterText);
        }
    }

    public RabatCn getRcnById(Long id) {
        return rabatCnRepo.findById(id).orElseThrow(() -> new RuntimeException("no rcn with this id"));
    }

    public List<ChangeNotice> getAllCnsOfRcn(RabatCn rcn) {
        return rabatCnRepo.findAllCnsOfRcn(rcn);
    }

    public List<RabatCn> getAllRcnsOfCn(ChangeNotice cn) {
        return rabatCnRepo.findAllByChangeNotices(cn);
    }

    public void deleteListOfRcns(List<RabatCn> rabatCnsOfProj) {
        rabatCnRepo.deleteAllById(rabatCnsOfProj.stream().map(RabatCn::getId).toList());
    }
}
