package com.lear.change_management.entities;

import com.github.javaparser.printer.lexicalpreservation.changes.Change;
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
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String partNumber;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Variant variant)) return false;

        return Objects.equals(getId(), variant.getId()) && Objects.equals(getPartNumber(), variant.getPartNumber());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getId());
        result = 31 * result + Objects.hashCode(getPartNumber());
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "project_id")
    @NotNull
    private Project project;

    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY)
    private Set<SetupPlan> setupPlans = new HashSet<>();

    @ManyToMany(mappedBy = "affectedVariants", fetch = FetchType.LAZY)
    private Set<RabatCn> rabatCns = new HashSet<>();


    @ManyToMany(mappedBy = "affectedVariants", fetch = FetchType.LAZY)
    private Set<ChangeNotice> cns = new HashSet<>();

    public void deleteCn(ChangeNotice changeNotice) {
        this.getCns().remove(changeNotice);
    }
}
