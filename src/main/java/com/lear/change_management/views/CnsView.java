package com.lear.change_management.views;

import com.lear.change_management.entities.ChangeNotice;
import com.lear.change_management.entities.RabatCn;
import com.lear.change_management.services.ChangeNoticeService;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Route(value = "CNs", layout = NestedLayout.class)
@Menu(title = "CNs", order = 4, icon = "vaadin:bullets")
@RolesAllowed("ADMIN")
public class CnsView extends VerticalLayout {

    @Autowired
    final ChangeNoticeService cnService;
    final RabatCnService rabatCnService;

    Grid<ChangeNotice> grid = new Grid<>(ChangeNotice.class, false);
    TextField filterText = new TextField();
    CnForm form;


    public CnsView(ChangeNoticeService cnService, RabatCnService rabatCnService) {
        this.cnService = cnService;
        this.rabatCnService = rabatCnService;
        addClassName("cns-view");
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
        form = new CnForm(rabatCnService.getAllRcns());
        form.setWidth("25em");
        form.addSaveListener(this::saveCn);
        form.addDeleteListener(this::deleteCn);
        form.addCloseListener(e -> closeEditor());
    }

    private HorizontalLayout getToolBar() {

        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addRcnButton = new Button("Add a CN");
        addRcnButton.addClickListener(click -> addCn());

        var toolbar = new HorizontalLayout(filterText, addRcnButton );
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addCn() {
        grid.asSingleSelect().clear();
        editCn(new ChangeNotice());
    }

    public void editCn(ChangeNotice changeNotice) {
        if (changeNotice == null) {
            closeEditor();
        } else {
            form.setCn(changeNotice);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setCn(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void configureGrid() {
        grid.setEmptyStateText("No Cns found.");
        grid.addClassNames("cn-grid");
        grid.setSizeFull();
        grid.setColumns("name", "nature", "description", "status");
        grid.addColumn(changeNotice -> changeNotice.getRabatCn().getName()).setHeader("RCN");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editCn(event.getValue()));
    }

    private void updateList() {
        grid.setItems(cnService.getAllCns(filterText.getValue()));
    }

    private void saveCn(CnForm.SaveEvent event) {
        ChangeNotice changeNotice = event.getChangeNotice();
        cnService.addCn(changeNotice);
        updateList();
        closeEditor();
    }
    private void deleteCn(CnForm.DeleteEvent event) {
        cnService.deleteCn(event.getChangeNotice());
        updateList();
        closeEditor();
    }


}
