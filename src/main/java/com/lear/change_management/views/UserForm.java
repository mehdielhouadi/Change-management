package com.lear.change_management.views;

import com.lear.change_management.entities.RabatCn;
import com.lear.change_management.entities.Role;
import com.lear.change_management.entities.User;
import com.lear.change_management.services.RoleService;
import com.lear.change_management.services.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

public class UserForm extends FormLayout {

    private final UserService userService;
    private final RoleService roleService;

    TextField name = new TextField("Name");
    TextField email = new TextField("email");
    ComboBox<Role> role = new ComboBox<>("role");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<User> binder = new BeanValidationBinder<>(User.class);

    public UserForm(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
        addClassName("user-form");
        binder.bindInstanceFields(this);

        role.setItems(roleService.getAll());
        role.setItemLabelGenerator(Role::getName);

        add(name, email, role, createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new UserForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new UserForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); // <4>
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new UserForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void setUser(User user) {
        binder.setBean(user);
    }


    // Events
    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private User user;

        protected UserFormEvent(UserForm source, User user) {
            super(source, false);
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public static class SaveEvent extends UserForm.UserFormEvent {

        SaveEvent(UserForm source, User user) {
            super(source, user);
        }
    }

    public static class DeleteEvent extends UserForm.UserFormEvent {
        DeleteEvent(UserForm source, User user) {
            super(source, user);
        }

    }

    public static class CloseEvent extends UserForm.UserFormEvent {
        CloseEvent(UserForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<UserForm.DeleteEvent> listener) {
        return addListener(UserForm.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<UserForm.SaveEvent> listener) {
        return addListener(UserForm.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<UserForm.CloseEvent> listener) {
        return addListener(UserForm.CloseEvent.class, listener);
    }


}
