package com.lear.change_management.views;

import com.lear.change_management.entities.ChangeNotice;
import com.lear.change_management.entities.Project;
import com.lear.change_management.entities.RabatCn;
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

import java.util.List;


public class CnForm extends FormLayout {

    TextField name = new TextField("CN Name");
    ComboBox<String> nature = new ComboBox<>("Nature");
    TextField description = new TextField("Description");
    ComboBox<String> status = new ComboBox<>("Status");
    ComboBox<RabatCn> rabatCn = new ComboBox<>("RabatCn");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<ChangeNotice> binder = new BeanValidationBinder<>(ChangeNotice.class);

    public CnForm(List<RabatCn> rabatCns) {
        addClassName("cn-form");
        binder.bindInstanceFields(this);
        nature.setItems("HW", "SW", "HW/SW");
        status.setItems("IN PROGRESS", "DONE");
        rabatCn.setItems(rabatCns);
        rabatCn.setItemLabelGenerator(RabatCn::getName);
        add(name, nature, description, status, rabatCn, createButtonsLayout());
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


    public void setCn(ChangeNotice changeNotice) {
        binder.setBean(changeNotice); // <1>
    }

    // Events
    public static abstract class CnFormEvent extends ComponentEvent<CnForm> {
        private ChangeNotice changeNotice;

        protected CnFormEvent(CnForm source, ChangeNotice changeNotice) {
            super(source, false);
            this.changeNotice = changeNotice;
        }

        public ChangeNotice getChangeNotice() {
            return changeNotice;
        }
    }

    public static class SaveEvent extends CnFormEvent {
        SaveEvent(CnForm source, ChangeNotice changeNotice) {
            super(source, changeNotice);
        }
    }

    public static class DeleteEvent extends CnFormEvent {
        DeleteEvent(CnForm source, ChangeNotice changeNotice) {
            super(source, changeNotice);
        }

    }

    public static class CloseEvent extends CnFormEvent {
        CloseEvent(CnForm source) {
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