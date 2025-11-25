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
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String PartNumber;

    private String status;

    @ManyToMany(mappedBy = "affectedVariants", fetch = FetchType.EAGER)
    private Set<RabatCn> rabatCns = new HashSet<>();

    @ManyToMany(mappedBy = "affectedVariants", fetch = FetchType.EAGER)
    private Set<ChangeNotice> changeNotices = new HashSet<>();

}
