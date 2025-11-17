package com.lear.change_management.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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

    private String name;

    @OneToMany(mappedBy = "rabatCn", fetch = FetchType.LAZY)
    private Set<ChangeNotice> changeNotices = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private String status;

    @Column(name = "creation_date")
    private LocalDate creationDate;

}
