package com.lear.change_management.views.variant;

import com.lear.change_management.entities.Project;
import com.lear.change_management.entities.Variant;
import com.lear.change_management.services.ProjectService;
import com.lear.change_management.services.VariantService;
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

public class VariantsForm extends FormLayout {

    private final VariantService variantsService;
    private final ProjectService projectService;

    TextField partNumber = new TextField("PN");
    ComboBox<Project> project = new ComboBox<>("project");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<Variant> binder = new BeanValidationBinder<>(Variant.class);

    public VariantsForm(VariantService variantsService, ProjectService projectService) {
        this.variantsService = variantsService;
        this.projectService = projectService;
        addClassName("variants-form");
        binder.bindInstanceFields(this);
        project.setItems(projectService.getAllProjects());
        project.setItemLabelGenerator(Project::getName);
        add(partNumber, project, createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new VariantsForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new VariantsForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new VariantsForm.SaveEvent(this, binder.getBean()));
        }
    }

    public void setVariant(Variant variant) {
        binder.setBean(variant);
    }


    // Events
    public static abstract class VariantsFormEvent extends ComponentEvent<VariantsForm> {
        private Variant variant;

        protected VariantsFormEvent(VariantsForm source, Variant variant) {
            super(source, false);
            this.variant = variant;
        }

        public Variant getVariant() {
            return variant;
        }
    }

    public static class SaveEvent extends VariantsForm.VariantsFormEvent {

        SaveEvent(VariantsForm source, Variant variant) {
            super(source, variant);
        }
    }

    public static class DeleteEvent extends VariantsForm.VariantsFormEvent {
        DeleteEvent(VariantsForm source, Variant variant) {
            super(source, variant);
        }

    }

    public static class CloseEvent extends VariantsForm.VariantsFormEvent {
        CloseEvent(VariantsForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<VariantsForm.DeleteEvent> listener) {
        return addListener(VariantsForm.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<VariantsForm.SaveEvent> listener) {
        return addListener(VariantsForm.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<VariantsForm.CloseEvent> listener) {
        return addListener(VariantsForm.CloseEvent.class, listener);
    }


}
