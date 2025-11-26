package com.lear.change_management.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String partNumber;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @NotNull
    private Project project;

    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY)
    private Set<SetupPlan> setupPlans = new HashSet<>();

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "variant_id"),
            inverseJoinColumns = @JoinColumn(name = "rcn_id"))
    private Set<RabatCn> rabatCns = new HashSet<>();

}
