package com.lear.change_management.views;

import com.lear.change_management.views.ui.NestedLayout;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "Rcns", layout = NestedLayout.class)
@Menu(title = "Rabat CNs", order = 3, icon = "vaadin:bullets")
public class RabatCnsView extends Main {
    public RabatCnsView() {

    }
}
