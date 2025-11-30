package com.lear.change_management.views.project;

import com.lear.change_management.entities.SetupStep;
import com.lear.change_management.entities.User;
import com.lear.change_management.services.ProjectService;
import com.lear.change_management.services.RabatCnService;
import com.lear.change_management.services.SetupStepService;
import com.lear.change_management.services.UserService;
import com.lear.change_management.views.users.UserForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.Map;

@Route(value = "/projects/processsteps/")
@RolesAllowed("ADMIN")
public class SetupStepsView extends VerticalLayout implements HasUrlParameter<String> {

    private Long projectId = 0L;
    Grid<SetupStep> grid = new Grid<>(SetupStep.class, false);
    SetupStepForm form;
    private final UserService userService;
    private final SetupStepService setupStepService;
    private final ProjectService projectService;


    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String s) {
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters
                .getParameters();
        List<String> projectIdList = parametersMap.get("id");
        String projectIdString = projectIdList.getFirst();
        try {
            projectId = Long.valueOf(projectIdString);
        } catch (NumberFormatException exception) {
            projectId = 0L;
        }
        configureForm();
        add(getToolBar(), getContent());
        updateList();
        closeEditor();
    }

    public SetupStepsView(UserService userService, SetupStepService setupStepService, ProjectService projectService) {
        this.userService = userService;
        this.setupStepService = setupStepService;
        this.projectService = projectService;
        setSizeFull();
        configureGrid();
    }

    private void configureForm() {
        List<User> allUsers = userService.getAll();
        form = new SetupStepForm(allUsers, projectId, projectService);
        form.setWidth("25em");
        form.addSaveListener(this::saveSetupStep);
        form.addDeleteListener(this::deleteSetupStep);
        form.addCloseListener(e -> closeEditor());
    }

    private void deleteSetupStep(SetupStepForm.DeleteEvent event) {
        setupStepService.deleteSetupSteps(event.getSetupStep());
        updateList();
        closeEditor();
    }

    private void saveSetupStep(SetupStepForm.SaveEvent event) {
        SetupStep setupStep = event.getSetupStep();
        setupStepService.addSetupStep(setupStep);
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
        Button addUserButton = new Button("Add a process step");
        addUserButton.addClickListener(click -> addSetupStep(projectId));
        var toolbar = new HorizontalLayout(addUserButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addSetupStep(Long projectId) {
        grid.asSingleSelect().clear();
        SetupStep ss = new SetupStep();
        ss.setProject(projectService.getProjectById(projectId));
        editSetupStep(ss);
    }
    public void editSetupStep(SetupStep setupStep) {
        if (setupStep == null) {
            closeEditor();
        } else {
            form.setSetupStep(setupStep);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setSetupStep(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        List<SetupStep> steps = setupStepService.getStepsByProjId(projectId);
        grid.setItems(steps);
    }

    private void configureGrid() {
        // Employee column (custom component renderer)
        grid.addColumn(SetupStep::getName)
                .setHeader("Name")
                .setAutoWidth(true)
                .setSortable(true)
                .setComparator(SetupStep::getName);
        grid.addColumn(setupStep -> setupStep.getProject().getName())
                .setHeader("Project")
                .setAutoWidth(true)
                .setSortable(true)
                .setComparator(SetupStep::getName);
        grid.addColumn(new ComponentRenderer<>(setupStep -> {
                        HorizontalLayout layout = new HorizontalLayout();
                                layout.setSpacing(true);
                                setupStep.getOwners().forEach(owner -> {
                                    Span chip = new Span(owner.getUserName());
                                    chip.getElement().getThemeList().add("badge");
                                    layout.add(chip);
                                });
                        return layout;
                        })
                    )
                .setHeader("Owners")
                .setAutoWidth(true)
                .setSortable(true)
                .setComparator(SetupStep::getName);

        grid.asSingleSelect().addValueChangeListener(event ->
                editSetupStep(event.getValue()));
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setItems(setupStepService.getAll());
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();
    }

}
