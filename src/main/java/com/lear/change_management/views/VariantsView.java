package com.lear.change_management.views;

import com.lear.change_management.views.ui.NestedLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
@Route(value = "variants", layout = NestedLayout.class)
@Menu(title = "variants", order = 4, icon = "vaadin:user")
@RolesAllowed("ADMIN")
public class VariantsView extends VerticalLayout {


}
