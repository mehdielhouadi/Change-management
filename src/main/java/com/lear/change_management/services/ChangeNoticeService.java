package com.lear.change_management.services;

import com.lear.change_management.entities.ChangeNotice;
import com.lear.change_management.repositories.ChangeNoticeRepo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ChangeNoticeService {

    @Autowired
    private ChangeNoticeRepo cnRepo;

    public List<ChangeNotice> getAllCns(String value) {
        return cnRepo.findAll();
    }

    public void addCn(ChangeNotice changeNotice) {
        cnRepo.save(changeNotice);
    }

    public void deleteCn(ChangeNotice changeNotice) {
        cnRepo.delete(changeNotice);
    }
}
