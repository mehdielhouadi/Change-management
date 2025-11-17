package com.lear.change_management.views;

import com.lear.change_management.entities.Project;
import com.lear.change_management.entities.RabatCn;
import com.sun.jna.platform.win32.WinBase;
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
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;


public class RcnForm extends FormLayout {


    TextField name = new TextField("RCN Name");
    TextField status = new TextField("Status");
    ComboBox<Project> project = new ComboBox<>("Project");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<RabatCn> binder = new BeanValidationBinder<>(RabatCn.class);

    public RcnForm(List<Project> projects) {
        addClassName("rcn-form");
        binder.bindInstanceFields(this);

        project.setItems(projects);
        project.setItemLabelGenerator(Project::getName);

        add(name, status, project, createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave()); // <1>
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean()))); // <2>
        close.addClickListener(event -> fireEvent(new CloseEvent(this))); // <3>

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); // <4>
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }


    public void setRcn(RabatCn rabatCn) {
        binder.setBean(rabatCn); // <1>
    }


    // Events
    public static abstract class RcnFormEvent extends ComponentEvent<RcnForm> {
        private RabatCn rabatCn;

        protected RcnFormEvent(RcnForm source, RabatCn rabatCn) {
            super(source, false);
            this.rabatCn = rabatCn;
        }

        public RabatCn getRabatCn() {
            return rabatCn;
        }
    }

    public static class SaveEvent extends RcnFormEvent {
        SaveEvent(RcnForm source, RabatCn rabatCn) {
            super(source, rabatCn);
        }
    }

        public static class DeleteEvent extends RcnFormEvent {
        DeleteEvent(RcnForm source, RabatCn rabatCn) {
            super(source, rabatCn);
        }

    }

    public static class CloseEvent extends RcnFormEvent {
        CloseEvent(RcnForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}