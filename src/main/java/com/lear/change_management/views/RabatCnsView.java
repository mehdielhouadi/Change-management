package com.lear.change_management.views;

import com.lear.change_management.entities.RabatCn;
import com.lear.change_management.services.ProjectService;
import com.lear.change_management.services.RabatCnService;
import com.lear.change_management.views.ui.NestedLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Route(value = "Rcns", layout = NestedLayout.class)
@Menu(title = "Rabat CNs", order = 3, icon = "vaadin:bullets")
@RolesAllowed("ADMIN")
public class RabatCnsView extends VerticalLayout {

    final RabatCnService rabatCnService;
    final ProjectService projectService;

    Grid<RabatCn> grid = new Grid<>(RabatCn.class, false);
    TextField filterText = new TextField();
    RcnForm form;


    public RabatCnsView(RabatCnService rabatCnService, ProjectService projectService) {
        this.rabatCnService = rabatCnService;
        this.projectService = projectService;
        addClassName("Rcns-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolBar(), getContent());
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

    private void configureForm() {
        form = new RcnForm(projectService.getAllProjects(), this.rabatCnService);
        form.setWidth("25em");
        form.addSaveListener(this::saveRcn);
        form.addDeleteListener(this::deleteRcn);
        form.addCloseListener(e -> closeEditor());
    }

    @NotNull
    private HorizontalLayout getToolBar() {

        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addRcnButton = new Button("Add a RCN");
        addRcnButton.addClickListener(click -> addRcn());

        var toolbar = new HorizontalLayout(filterText, addRcnButton );
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addRcn() {
        grid.asSingleSelect().clear();
        RabatCn rcn = new RabatCn();
        rcn.setName("RCN - " +  rabatCnService.getCount());
        editRcn(rcn);
    }

    public void editRcn(RabatCn rabatCn) {
        if (rabatCn == null) {
            closeEditor();
        } else {
            form.setRcn(rabatCn);

            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setRcn(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void configureGrid() {
        grid.setEmptyStateText("No RCns found.");
        grid.addClassNames("rcn-grid");
        grid.setSizeFull();
        grid.setColumns("name", "status", "creationDate");
        grid.addColumn(rcn -> rcn.getProject().getName()).setHeader("Project");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editRcn(event.getValue()));
    }

    private void updateList() {
        grid.setItems(rabatCnService.getAllRcns(filterText.getValue()));
    }

    private void saveRcn(RcnForm.SaveEvent event) {
        RabatCn rabatCn = event.getRabatCn();
        if (null == rabatCn.getCreationDate()) rabatCn.setCreationDate(LocalDate.now());
        rabatCnService.addRcn(rabatCn);
        updateList();
        closeEditor();
    }
    private void deleteRcn(RcnForm.DeleteEvent event) {
        rabatCnService.deleteRcn(event.getRabatCn());
        updateList();
        closeEditor();
    }


}
