package com.lear.change_management.services;

import com.lear.change_management.entities.Project;
import com.lear.change_management.entities.Variant;
import com.lear.change_management.repositories.VariantRepo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VariantService {

    @Autowired
    private VariantRepo variantRepo;

    public List<Variant> getAll() {
        return variantRepo.findAll();
    }

    public void addVariant(Variant variant) {
        variantRepo.save(variant);
    }

    public void deleteVariant(Variant variant) {
        variantRepo.delete(variant);
    }

    public List<Variant> getAll(String filterText) {
        if (null==filterText || filterText.isEmpty()) {
            return variantRepo.findAll();
        }
        else {
                return variantRepo.findAllFiltered(filterText);
        }
    }

    public List<Variant> getVariantsOfProject(Project selectedProjects) {
        return variantRepo.findAllByProject(selectedProjects);
    }
}
