package com.lear.change_management.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetupEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // for info
    @ManyToOne
    @JoinColumn(name = "setupStep_id")
    private SetupStep setupStep;

    private String status;

    private boolean isAffected;

    @ManyToOne
    @JoinColumn(name = "responsible_id")
    private User responsible;

    private LocalDate startDate;

    private LocalDate dueDate;

    private String commentSignature;

    @ManyToOne
    @JoinColumn(name = "setupPlan_id")
    private SetupPlan setupPlan;

}
