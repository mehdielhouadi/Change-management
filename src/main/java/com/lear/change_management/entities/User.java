package com.lear.change_management.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotEmpty
    private String userName;

    @NotNull
    @NotEmpty
    private String password;

    @Email
    @NotNull
    @NotEmpty
    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "setupStep_id")
    )
    private Set<SetupStep> SetupStepsAssigned = new HashSet<>();

}
