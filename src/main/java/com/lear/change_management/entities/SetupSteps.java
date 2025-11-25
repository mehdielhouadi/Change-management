package com.lear.change_management.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class SetupSteps {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    private boolean isAffected;

    @ManyToOne
    @JoinColumn(name = "responsible_id")
    private User responsible;

    private LocalDate startDate;

    private LocalDate dueDate;

    private String status;

    private String commentSignature;

    @ManyToOne
    @JoinColumn(name = "setupstep_id")
    @NotNull
    private Project project;


}
