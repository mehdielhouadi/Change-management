package com.lear.change_management.services;

import com.lear.change_management.entities.SetupStep;
import com.lear.change_management.repositories.SetupStepRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetupStepService {

    @Autowired
    private SetupStepRepo setupStepRepo;

    public List<SetupStep> getStepsByProjId(Long id) {
        return setupStepRepo.findByProjectIdWithOwners(id);
    }

    public void deleteSetupSteps(SetupStep setupStep) {
        setupStepRepo.delete(setupStep);
    }

    public void addSetupStep(SetupStep setupStep) {

        setupStepRepo.save(setupStep);
    }

    public List<SetupStep> getAll() {
        return setupStepRepo.findAll();
    }
}
