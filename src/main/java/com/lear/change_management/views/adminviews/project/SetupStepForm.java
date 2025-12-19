package com.lear.change_management.views.adminviews.project;

import com.lear.change_management.entities.*;
import com.lear.change_management.services.ProjectService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;
import java.util.Set;

public class SetupStepForm extends FormLayout {

    TextField name = new TextField("Step Name");
    TextField project = new TextField("Project");
    MultiSelectComboBox<User> owners = new MultiSelectComboBox<>("owners");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<SetupStep> binder = new BeanValidationBinder<>(SetupStep.class);
    final ProjectService projectService;

    public SetupStepForm(List<User> users, Long projectId, ProjectService projectService) {
        this.projectService = projectService;

        binder.forField(name).bind( SetupStep::getName, SetupStep::setName);
        binder.forField(project).bind(
                setupStep -> projectService.getProjectById(projectId).getName(),
                (setupStep, value) -> {}
        );
        project.setReadOnly(true);

        binder.forField(owners)
                .bind(
                        SetupStep::getOwners,
                        SetupStep::setOwners
                );
        this.owners.setLabel("Owners");
        this.owners.setItems(users);
        this.owners.setItemLabelGenerator(User::getUserName);
        this.owners.setAutoExpand(MultiSelectComboBox.AutoExpandMode.BOTH);
        this.owners.addValueChangeListener(event -> {
            Set<User> selectedUsers = event.getValue();
        });
        add(name, project, owners, createButtonsLayout());
    }


    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave()); // <1>
        delete.addClickListener(event -> fireEvent(new SetupStepForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new SetupStepForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); // <4>
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SetupStepForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void setSetupStep(SetupStep setupStep) {
        binder.setBean(setupStep); // <1>
    }

    // Events
    public static abstract class SetupStepFormEvent extends ComponentEvent<SetupStepForm> {
        private SetupStep setupStep;

        protected SetupStepFormEvent(SetupStepForm source, SetupStep setupStep) {
            super(source, false);
            this.setupStep = setupStep;
        }

        public SetupStep getSetupStep() {
            return setupStep;
        }
    }

    public static class SaveEvent extends SetupStepForm.SetupStepFormEvent {
        SaveEvent(SetupStepForm source, SetupStep setupStep) {
            super(source, setupStep);
        }
    }

    public static class DeleteEvent extends SetupStepForm.SetupStepFormEvent {
        DeleteEvent(SetupStepForm source, SetupStep setupStep) {
            super(source, setupStep);
        }

    }

    public static class CloseEvent extends SetupStepForm.SetupStepFormEvent {
        CloseEvent(SetupStepForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<SetupStepForm.DeleteEvent> listener) {
        return addListener(SetupStepForm.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SetupStepForm.SaveEvent> listener) {
        return addListener(SetupStepForm.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<SetupStepForm.CloseEvent> listener) {
        return addListener(SetupStepForm.CloseEvent.class, listener);
    }
}
