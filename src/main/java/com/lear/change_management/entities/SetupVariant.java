package com.lear.change_management.entities;

import jakarta.persistence.*;
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
public class SetupVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private Variant variant;

    private String status;

    private boolean isAffected;

    @ManyToOne
    @JoinColumn(name = "responsible_id")
    private User responsible;

    private LocalDate startDate;

    private LocalDate dueDate;

    private String commentSignature;

    @ManyToMany(mappedBy = "setupVariants", fetch = FetchType.LAZY)
    private Set<ChangeNotice> changeNotices = new HashSet<>();


}
