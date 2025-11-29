package com.lear.change_management;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.theme.Theme;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Theme("my-theme")
public class ChangeManagementApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(ChangeManagementApplication.class, args);
    }
}
