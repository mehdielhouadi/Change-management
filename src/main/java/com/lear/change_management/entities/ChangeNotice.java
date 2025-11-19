package com.lear.change_management.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "rabat_cn_id")
    private RabatCn rabatCn;


}
