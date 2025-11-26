package com.lear.change_management.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetupStep {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToMany(mappedBy = "SetupStepsAssigned", fetch = FetchType.LAZY)
    private Set<User> owners;

    @OneToMany(mappedBy = "setupStep", fetch = FetchType.LAZY)
    private Set<SetupEntry> setupEntries= new HashSet<>();
}
