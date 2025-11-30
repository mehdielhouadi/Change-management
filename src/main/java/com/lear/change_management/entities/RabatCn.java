package com.lear.change_management.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RabatCn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @NotNull
    private String name;

    @ManyToMany(mappedBy = "rabatCns", fetch = FetchType.LAZY)
    private Set<ChangeNotice> changeNotices = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "rcn_id"),
            inverseJoinColumns = @JoinColumn(name = "variant_id"))
    private Set<Variant> affectedVariants = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    @NotNull
    private Project project;

    @NotNull
    private String status;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof RabatCn rabatCn)) return false;

        return Objects.equals(getId(), rabatCn.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
