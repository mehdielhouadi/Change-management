package com.lear.change_management.views;

import com.lear.change_management.entities.Project;
import com.lear.change_management.entities.RabatCn;
import com.lear.change_management.services.ProjectService;
import com.lear.change_management.services.RabatCnService;
import com.lear.change_management.views.ui.NestedLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
import org.springframework.dao.DataIntegrityViolationException;

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
                ConfirmDialog dialog = new ConfirmDialog();
                dialog.setHeader("Delete project");
                dialog.setText("Are you sure you want to permanently delete project \"" + project.getName() + "\" and all its RCNs?");
                dialog.setCancelable(true);
                dialog.setConfirmText("Delete");

                dialog.addConfirmListener(event -> {
                    // Delete all RCNs related to the project
                    projectService.getProjectWithRcns(project)
                            .forEach(p -> p.getRabatCns().forEach(rabatCnService::deleteRcn));

                    projectService.deleteProject(project.getId());

                    refreshGrid("");
                    Notification.show("Project and related RCns deleted.", 2000, Notification.Position.MIDDLE);
                });
                dialog.open();
            });

            actions.add(edit, delete);
            return actions;
        }).setHeader("Actions");
        grid.setDetailsVisibleOnClick(false);
        grid.setItemDetailsRenderer(new ComponentRenderer<>(this::createRcnCardList));
        refreshGrid("");
        return grid;
    }


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

        // Clear and populate the field if editing
        nameField.clear();
        nameField.getElement().getThemeList().remove("error");
        if (project != null) {
            nameField.setValue(project.getName());
        }

        // Save button
        Button save = new Button("Save", e -> {
            String projectName = nameField.getValue().trim();

            // 1️⃣ Check empty
            if (projectName.isEmpty()) {
                nameField.getElement().getThemeList().add("error");
                Notification.show("Project name cannot be empty.", 3000, Notification.Position.MIDDLE);
                return;
            } else {
                nameField.getElement().getThemeList().remove("error");
            }

            // 2️⃣ Check duplicates (case-insensitive)
            boolean exists = projectService.existsByNameIgnoreCase(projectName);
            if (exists && (selectedProject == null || !selectedProject.getName().equalsIgnoreCase(projectName))) {
                nameField.getElement().getThemeList().add("error");
                Notification.show("Project with this name already exists.", 3000, Notification.Position.MIDDLE);
                return;
            } else {
                nameField.getElement().getThemeList().remove("error");
            }

            // 3️⃣ Try saving to DB
            try {
                if (selectedProject == null) {
                    Project p = new Project();
                    p.setName(projectName);
                    projectService.addProject(p);
                } else {
                    selectedProject.setName(projectName);
                    projectService.addProject(selectedProject);
                }

                dialog.close();
                refreshGrid("");
            } catch (DataIntegrityViolationException ex) {
                nameField.getElement().getThemeList().add("error");
                Notification.show("Project with this name already exists.", 3000, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                Notification.show("Error saving project: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        // Cancel button
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

