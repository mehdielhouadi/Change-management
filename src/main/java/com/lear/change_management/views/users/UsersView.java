package com.lear.change_management.views.users;


import com.lear.change_management.entities.User;
import com.lear.change_management.services.PasswordResetService;
import com.lear.change_management.services.RoleService;
import com.lear.change_management.services.UserService;
import com.lear.change_management.views.ui.NestedLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

@Route(value = "users", layout = NestedLayout.class)
@Menu(title = "users", order = 4, icon = "vaadin:user")
@RolesAllowed("ADMIN")
public class UsersView extends VerticalLayout {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetService passwordResetService;

    Grid<User> grid = new Grid<>(User.class, false);
    TextField filterText = new TextField();
    UserForm form;




    public UsersView(UserService userService, RoleService roleService,
                     PasswordEncoder passwordEncoder, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordResetService = passwordResetService;
        this.passwordEncoder = passwordEncoder;
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolBar(), getContent());
        updateList();
        closeEditor();
    }

    private void configureForm() {
        form = new UserForm(userService, roleService);
        form.setWidth("25em");
        form.addSaveListener(this::saveUser);
        form.addDeleteListener(this::deleteUser);
        form.addCloseListener(e -> closeEditor());
    }

    private void deleteUser(UserForm.DeleteEvent event) {
        userService.deleteUser(event.getUser());
        updateList();
        closeEditor();
    }

    @Transactional
    private void saveUser(UserForm.SaveEvent event) {
        User user = event.getUser();

        String randomPassword = RandomStringUtils.randomAlphanumeric(32);
        user.setPassword(passwordEncoder.encode(randomPassword));
        passwordResetService.createAndSendResetToken(user);

        userService.addUser(user);
        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolBar() {

        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addUserButton = new Button("Add a user");
        addUserButton.addClickListener(click -> addUser());

        var toolbar = new HorizontalLayout(filterText, addUserButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        editUser(new User());
    }
    public void editUser(User user) {
        if (user == null) {
            closeEditor();
        } else {
            form.setUser(user);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setUser(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(userService.getAll(filterText.getValue()));
    }

    private void configureGrid() {
        // Employee column (custom component renderer)
        grid.addColumn(User::getUserName)
                .setHeader("Employee")
                .setAutoWidth(true)
                .setSortable(true)
                .setComparator(User::getUserName);
        grid.addColumn(User::getEmail)
                .setHeader("Email")
                .setAutoWidth(true)
                .setSortable(true)
                .setComparator(User::getEmail);

        grid.addColumn(u -> u.getRole().getName().toLowerCase().replace("role_", ""))
                .setHeader("Role")
                .setAutoWidth(true)
                .setSortable(true);

        grid.asSingleSelect().addValueChangeListener(event ->
                editUser(event.getValue()));
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setItems(userService.getAll());
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();
    }

}
