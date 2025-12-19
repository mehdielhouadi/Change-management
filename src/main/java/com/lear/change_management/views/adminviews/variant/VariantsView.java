package com.lear.change_management.views.adminviews.variant;

import com.lear.change_management.entities.Variant;
import com.lear.change_management.services.ProjectService;
import com.lear.change_management.services.RoleService;
import com.lear.change_management.services.UserService;
import com.lear.change_management.services.VariantService;
import com.lear.change_management.views.adminviews.ui.NestedLayout;
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
@Route(value = "variants", layout = NestedLayout.class)
@Menu(title = "variants", order = 4, icon = "vaadin:user")
@RolesAllowed("ADMIN")
public class VariantsView extends VerticalLayout {

    private final VariantService variantService;
    private final ProjectService projectService;
    Grid<Variant> grid = new Grid<>(Variant.class, false);
    TextField filterText = new TextField();
    VariantsForm form;

    public VariantsView(UserService userService, RoleService roleService, VariantService variantService, ProjectService projectService) {
        this.variantService = variantService;
        this.projectService = projectService;
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolBar(), getContent());
        updateList();
        closeEditor();
    }

    private void configureForm() {
        form = new VariantsForm(variantService, projectService);
        form.setWidth("25em");
        form.addSaveListener(this::saveVariant);
        form.addDeleteListener(this::deleteVariant);
        form.addCloseListener(e -> closeEditor());
    }

    private void deleteVariant(VariantsForm.DeleteEvent event) {
        variantService.deleteVariant(event.getVariant());
        updateList();
        closeEditor();
    }

    private void saveVariant(VariantsForm.SaveEvent event) {
        Variant variant = event.getVariant();
        variantService.addVariant(variant);
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

        Button addVariantButton = new Button("Add a variant");
        addVariantButton.addClickListener(click -> addVariant());

        var toolbar = new HorizontalLayout(filterText, addVariantButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addVariant() {
        grid.asSingleSelect().clear();
        editVariant(new Variant());
    }
    public void editVariant(Variant variant) {
        if (variant == null) {
            closeEditor();
        } else {
            form.setVariant(variant);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setVariant(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(variantService.getAll(filterText.getValue()));
    }

    private void configureGrid() {
        // Employee column (custom component renderer)
        grid.addColumn(Variant::getPartNumber)
                .setHeader("Part Number")
                .setAutoWidth(true)
                .setSortable(true)
                .setComparator(Variant::getPartNumber);
        grid.addColumn(variant -> variant.getProject().getName())
                .setHeader("Project")
                .setAutoWidth(true)
                .setSortable(true)
                .setComparator(v -> v.getProject().getName());

        grid.asSingleSelect().addValueChangeListener(event ->
                editVariant(event.getValue()));
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setItems(variantService.getAll());
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();
    }

}
