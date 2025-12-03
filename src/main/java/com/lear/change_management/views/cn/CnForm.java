package com.lear.change_management.views.cn;

import com.lear.change_management.entities.ChangeNotice;
import com.lear.change_management.entities.Project;
import com.lear.change_management.entities.RabatCn;
import com.lear.change_management.entities.Variant;
import com.lear.change_management.services.RabatCnService;
import com.lear.change_management.services.VariantService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;


public class CnForm extends FormLayout {

    TextField name = new TextField("CN Name");
    ComboBox<String> nature = new ComboBox<>("Nature");
    TextField description = new TextField("Description");
    ComboBox<String> status = new ComboBox<>("Status");
    //CheckboxGroup<Project> projects = new CheckboxGroup<>("Projects");
    MultiSelectComboBox<RabatCn> rabatCns = new MultiSelectComboBox<>("RabatCn");
    ComboBox<Project> project = new ComboBox<>("projects");
    MultiSelectComboBox<Variant> variants = new MultiSelectComboBox<>("Variants");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<ChangeNotice> binder = new BeanValidationBinder<>(ChangeNotice.class);

    final RabatCnService rcnService;
    final VariantService variantService;

    public CnForm(List<Project> projects, RabatCnService rcnService, VariantService variantService) {
        this.rcnService = rcnService;
        this.variantService = variantService;
        addClassName("cn-form");

        binder.forField(name)
                .bind(ChangeNotice::getName, ChangeNotice::setName);
        binder.forField(nature)
                .bind(ChangeNotice::getNature, ChangeNotice::setNature);
        binder.forField(description)
                .bind(ChangeNotice::getDescription, ChangeNotice::setDescription);
        binder.forField(status)
                .bind(ChangeNotice::getStatus, ChangeNotice::setStatus);
        binder.forField(rabatCns)
                .bind(ChangeNotice::getRabatCns, ChangeNotice::setRabatCns);
        binder.forField(project)
                .bind(ChangeNotice::getProject, ChangeNotice::setProject);
        binder.forField(variants)
                .bind(ChangeNotice::getAffectedVariants, ChangeNotice::setAffectedVariants);

        nature.setItems("HW", "SW", "HW/SW");
        status.setItems("IN PROGRESS", "DONE");

        this.project.setLabel("Select projects");
        this.project.setItems(projects);
        this.project.setItemLabelGenerator(Project::getName);

        this.rabatCns.setLabel("Select RCNs");
        this.rabatCns.setItemLabelGenerator(RabatCn::getName);
        List<RabatCn> rcns = rcnService.getAllRcns();
        this.rabatCns.setItems(rcns);

        this.variants.setItemLabelGenerator(Variant::getPartNumber);
        List<Variant> vars = variantService.getAll();
        this.variants.setItems(vars);

        this.project.addValueChangeListener(event -> {
            Project selectedProject = event.getValue();
            List<RabatCn> filtered = rcns.stream()
                    .filter(rabatCn -> selectedProject.getRabatCns().contains(rabatCn))
                    .toList();
            this.rabatCns.setItems(filtered);
            this.rabatCns.clear();
        });

        this.rabatCns.addValueChangeListener(event -> {
            Set<RabatCn> oldRcns = event.getOldValue();
            List<Variant> oldVars = oldRcns.stream()
                    .flatMap(rcn -> rcn.getAffectedVariants().stream())
                    .toList();
            List<RabatCn> selectedRcns = event
                    .getValue()
                    .stream()
                    .toList();


            // TODO
            List<Variant> filtered = vars
                    .stream()
                    .filter(var -> selectedRcns
                            .stream()
                            .anyMatch(rabatCn -> rabatCn.getAffectedVariants().contains(var)))
                    .toList();
//            List<Variant> combined = Stream.concat(filtered.stream(), oldVars.stream())
//                    .distinct()
//                    .toList();
            this.variants.setValue(oldVars);
            this.variants.setItems(filtered);
        });


        add(name, nature,description, status, this.project, this.rabatCns, variants, createButtonsLayout());
    }


    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave()); // <1>
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
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
        binder.setBean(changeNotice);
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