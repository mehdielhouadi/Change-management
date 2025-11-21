package com.lear.change_management.views;

import com.lear.change_management.entities.ChangeNotice;
import com.lear.change_management.entities.Project;
import com.lear.change_management.entities.RabatCn;
import com.lear.change_management.services.ChangeNoticeService;
import com.lear.change_management.services.ProjectService;
import com.lear.change_management.services.RabatCnService;
import com.lear.change_management.views.ui.NestedLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Route(value = "CNs", layout = NestedLayout.class)
@Menu(title = "CNs", order = 4, icon = "vaadin:bullets")
@RolesAllowed({"ADMIN", "ENGINEERING"})
public class CnsView extends VerticalLayout {

    final ChangeNoticeService cnService;
    final RabatCnService rabatCnService;
    final ProjectService projectService;

    Grid<ChangeNotice> grid = new Grid<>(ChangeNotice.class, false);
    TextField filterText = new TextField();
    CnForm form;


    public CnsView(ChangeNoticeService cnService, RabatCnService rabatCnService, ProjectService projectService) {
        this.cnService = cnService;
        this.rabatCnService = rabatCnService;
        this.projectService = projectService;
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
        List<Project> projectSet = projectService.getAllProjects();

        form = new CnForm(projectSet, this.rabatCnService);
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
        grid.setItemDetailsRenderer(new ComponentRenderer<>(this::createRcnCardList));
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editCn(event.getValue()));
    }

    private Component createRcnCardList(ChangeNotice changeNotice) {
        VerticalLayout container = new VerticalLayout();
        container.setPadding(false);
        container.setSpacing(true);
        container.setWidthFull();
        container.getStyle().set("background", "var(--lumo-contrast-5pct)");
        container.getStyle().set("border-radius", "var(--lumo-border-radius-m)");
        container.getStyle().set("padding", "1rem");

        // Add title
        Span title = new Span("RCNs for " + LocalDate.now().getYear());
        title.getStyle().set("font-weight", "600");
        title.getStyle().set("font-size", "var(--lumo-font-size-m)");
        container.add(title);

        if (changeNotice.getRabatCns().isEmpty()) {
            Span empty = new Span("No RCNs for this project.");
            empty.getStyle().set("color", "var(--lumo-secondary-text-color)");
            container.add(empty);
            return container;
        }

        // Layout for cards with wrapping
        HorizontalLayout row = new HorizontalLayout();
        row.setWidthFull();
        row.setSpacing(true);
        row.setWrap(true);

        int cardCount = 0;

        for (RabatCn rcn : changeNotice.getRabatCns()) {
            // Card container
            VerticalLayout card = new VerticalLayout();
            card.setPadding(true);
            card.setWidth("23%"); // approx 4 cards per row with spacing
            card.setSpacing(true);
            card.addClassName("rcn-card");
            card.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");
            card.getStyle().set("box-sizing", "border-box");

            // Title
            Span cardTitle = new Span(rcn.getName());
            cardTitle.addClassName("rcn-title");
            cardTitle.getStyle().set("font-weight", "600");
            cardTitle.getStyle().set("font-size", "var(--lumo-font-size-s)");

            // Status badge
            Span status = new Span(rcn.getStatus());
            switch (rcn.getStatus()) {
                case "APPROVED" -> status.getElement().getThemeList().add("badge success");
                case "REJECTED" -> status.getElement().getThemeList().add("badge error");
                case "PENDING" -> status.getElement().getThemeList().add("badge contrast");
                default -> status.getElement().getThemeList().add("badge");
            }

            // Creation date
            Span created = new Span("Created: " + rcn.getCreationDate());
            created.getStyle().set("color", "var(--lumo-secondary-text-color)");

            // Change notice count
            int count = rcn.getChangeNotices() == null ? 0 : rcn.getChangeNotices().size();
            Span cnCount = new Span("Change Notices: " + count);
            cnCount.getStyle().set("color", "var(--lumo-secondary-text-color)");

            // Top row layout (title + badge)
            HorizontalLayout header = new HorizontalLayout(cardTitle, status);
            header.setWidthFull();
            header.setAlignItems(FlexComponent.Alignment.CENTER);
            header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

            // Details section layout
            VerticalLayout details = new VerticalLayout(created, cnCount);
            details.setSpacing(false);
            details.setPadding(false);

            card.add(header, details);

            row.add(card);
            cardCount++;
        }

        container.add(row);
        return container;
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
