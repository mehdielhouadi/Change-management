package com.lear.change_management.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<SetupStep> setupSteps = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<RabatCn> rabatCns = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    private Set<Variant> variants = new HashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Project project)) return false;

        return Objects.equals(getId(), project.getId()) && Objects.equals(getName(), project.getName());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getId());
        result = 31 * result + Objects.hashCode(getName());
        return result;
    }
}
