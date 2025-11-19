package com.lear.change_management.views;

import com.lear.change_management.views.ui.NestedLayout;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "users", layout = NestedLayout.class)
@Menu(title = "users", order = 4, icon = "vaadin:user")
@RolesAllowed("ADMIN")
public class UsersView extends Main {
    public UsersView(){

    }
}
