package com.lear.change_management.views;

import com.lear.change_management.views.ui.NestedLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "/dashboard", layout = NestedLayout.class)
@RouteAlias("")
@Menu(title = "Dashboard", order = 1, icon = "vaadin:dashboard")
public class DashboardView extends Main {
    public DashboardView() {

    }
}
