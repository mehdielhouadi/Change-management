package com.lear.change_management.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
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
    @Column(unique = true)
    private String userName;

    @NotNull
    @NotEmpty
    private String password;

    @Email
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @NotNull
    private Role role;

    @ManyToMany(mappedBy = "owners", fetch = FetchType.EAGER)
    private Set<SetupStep> SetupStepsAssigned = new HashSet<>();

    private boolean mustChangePassword = true;

    @Column(unique = true)
    private String passwordResetToken;

    private LocalDateTime passwordResetExpiry;

    public User(User eventUser) {
        this.userName = eventUser.getUserName();
        this.email = eventUser.getEmail();
        this.password = eventUser.getPassword();
        this.role = eventUser.getRole();
    }
}
