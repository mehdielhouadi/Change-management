package com.lear.change_management.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class ChangeNotice {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    // HW SW HW/SW
    private String nature;

    private String description;

    private String status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "cn_id"),
            inverseJoinColumns = @JoinColumn(name = "rcn_id"))
    private Set<RabatCn> rabatCns = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "cn_id"),
            inverseJoinColumns = @JoinColumn(name = "variant_id"))
    private Set<Variant> affectedVariants = new HashSet<>();

    @OneToMany(mappedBy = "cn", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<SetupPlan> setupPlans = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    @NotNull
    private Project project;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ChangeNotice that)) return false;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
