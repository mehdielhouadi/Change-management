package com.lear.change_management.security;

import com.lear.change_management.views.DashboardView;
import com.lear.change_management.views.LoginView;
import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .with(VaadinSecurityConfigurer.vaadin(), configurer -> {
                        configurer.loginView(LoginView.class);
                })
                .logout(
                logout -> logout.logoutUrl("/logout")
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var uds = new InMemoryUserDetailsManager();
        var u1 = User.withUsername("ADMIN")
                .password("ADMIN").roles("ADMIN").build();
        uds.createUser(u1);
        var u2 = User.withUsername("PROD")
                .password("PROD").roles("PROD").build();
        uds.createUser(u2);
        return uds;
    }

    @Bean
    public PasswordEncoder encoder() {
        return  NoOpPasswordEncoder.getInstance();
    }


}
