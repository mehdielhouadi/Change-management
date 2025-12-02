package com.lear.change_management.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetupPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private Variant variant;

    @OneToMany(mappedBy = "setupPlan", fetch = FetchType.LAZY)
    private Set<SetupEntry> setupEntries = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "cn_id")
    private ChangeNotice cn;



    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof SetupPlan setupPlan)) return false;

        return getId().equals(setupPlan.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
