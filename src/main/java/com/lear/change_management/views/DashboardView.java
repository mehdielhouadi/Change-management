package com.lear.change_management.views;

import com.lear.change_management.views.ui.NestedLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "/home", layout = NestedLayout.class)
@RouteAlias("/")
@Menu(title = "Home", order = 1, icon = "vaadin:dashboard")
@RolesAllowed({"ADMIN", "PROD"})
public class DashboardView extends Main {
    public DashboardView() {
        add(new H1("Dashboard"));
    }
}
