package com.lear.change_management.views;

import com.lear.change_management.entities.Project;
import com.lear.change_management.entities.RabatCn;
import com.lear.change_management.services.ProjectService;
import com.lear.change_management.services.RabatCnService;
import com.lear.change_management.views.ui.NestedLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Route(value = "projects", layout = NestedLayout.class)
@Menu(title = "Projects", order = 2, icon = "vaadin:briefcase")
public class ProjectsView extends VerticalLayout {

    @Autowired
    private final ProjectService projectService;
    @Autowired
    private final RabatCnService rabatCnService;

    private Grid<Project> grid = new Grid<>(Project.class, false);
    private TextField nameField = new TextField("Project name");
    private Project selectedProject = null;
    TextField filterText = new TextField();

    public ProjectsView(ProjectService projectService, RabatCnService rabatCnService) {
        this.projectService = projectService;
        this.rabatCnService = rabatCnService;

        setSizeFull();
        add(createToolbar(), createGrid());
    }

    private Component createToolbar() {

        filterText.setPlaceholder("Filter");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> refreshGrid(filterText.getValue()));

        Button addButton = new Button("Add Project", VaadinIcon.PLUS.create());
        addButton.addClickListener(e -> openProjectDialog(null));

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addButton);

        return toolbar;
    }

    private Component createGrid() {
        grid.setEmptyStateText("No projects found.");
        grid.addColumn(createToggleDetailsRenderer(grid)).setWidth("80px")
                .setFlexGrow(0).setFrozen(true);
        grid.addColumn("name");
        grid.addComponentColumn(project -> {
            HorizontalLayout actions = new HorizontalLayout();

            Button edit = new Button("Edit", VaadinIcon.EDIT.create());
            edit.addClickListener(e -> openProjectDialog(project));

            Button delete = new Button("Delete", VaadinIcon.TRASH.create());
            delete.addClickListener(e -> {
                projectService.deleteProject(project.getId());
                refreshGrid("");
            });

            actions.add(edit, delete);
            return actions;
        }).setHeader("Actions");
        grid.setDetailsVisibleOnClick(false);
        grid.setItemDetailsRenderer(new ComponentRenderer<>(this::createRcnCardList));
        refreshGrid("");
        return grid;
    }

//    private Component createRcnSubGrid(Project project) {
//        VerticalLayout layout = new VerticalLayout();
//        layout.setPadding(false);
//        layout.setSpacing(false);
//
//        Grid<RabatCn> subGrid = new Grid<>(RabatCn.class, false);
//        subGrid.setEmptyStateText("No RCN found for this project this year.");
//
//        subGrid.setItems(project.getRabatCns());
//
//        subGrid.addColumn(RabatCn::getName).setHeader("Name");
//        subGrid.addColumn(RabatCn::getStatus).setHeader("Status");
//
//        layout.add(subGrid);
//        return layout;
//    }

    private static Renderer<Project> createToggleDetailsRenderer(Grid<Project> grid) {

        return LitRenderer.<Project>of("""
            <vaadin-button
                theme="tertiary icon"
                aria-label="Toggle details"
                aria-expanded="${model.detailsOpened ? 'true' : 'false'}"
                @click="${handleClick}"
            >
                <vaadin-icon
                    icon="${model.detailsOpened ? 'lumo:angle-down' : 'lumo:angle-right'}"
                ></vaadin-icon>
            </vaadin-button>
        """)
                .withFunction("handleClick",
                        p -> grid.setDetailsVisible(p, !grid.isDetailsVisible(p)));
    }

    private Component createRcnCardList(Project project) {
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

        if (project.getRabatCns().isEmpty()) {
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

        for (RabatCn rcn : project.getRabatCns()) {

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

            if (count > 0) {
                details.add(new Hr());

                VerticalLayout cnList = new VerticalLayout();
                cnList.setPadding(false);
                cnList.setSpacing(false);

                rcn.getChangeNotices().forEach(cn -> {
                    Span line = new Span("• " + cn.getName());
                    line.getStyle().set("margin-left", "0.5rem");
                    cnList.add(line);
                });

                details.add(cnList);
            }

            card.add(header, details);

            row.add(card);
            cardCount++;
        }

        container.add(row);
        return container;
    }


    private void openProjectDialog(Project project) {
        selectedProject = project;

        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        nameField.clear();
        if (project != null) {
            nameField.setValue(project.getName());
        }

        Button save = new Button("Save", e -> {
            if (selectedProject == null) {
                // add
                Project p = new Project();
                p.setName(nameField.getValue());
                projectService.addProject(p);
            } else {
                // update
                selectedProject.setName(nameField.getValue());
                projectService.addProject(selectedProject); // save
            }

            dialog.close();
            refreshGrid("");
        });

        Button cancel = new Button("Cancel", e -> dialog.close());

        dialog.add(
                new VerticalLayout(
                        nameField,
                        new HorizontalLayout(save, cancel)
                )
        );

        dialog.open();
    }

    private void refreshGrid(String filterText) {
        int currentYear = LocalDate.now().getYear();
        grid.setItems(projectService.getAllProjectsWithRcnsForYear(currentYear, filterText));
    }
}

