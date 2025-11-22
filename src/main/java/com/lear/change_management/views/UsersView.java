package com.lear.change_management.views;

import com.lear.change_management.entities.User;
import com.lear.change_management.services.UserService;
import com.lear.change_management.views.ui.NestedLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "users", layout = NestedLayout.class)
@Menu(title = "users", order = 4, icon = "vaadin:user")
@RolesAllowed("ADMIN")
public class UsersView extends Main {

    private final UserService userService;

    Grid<User> grid = new Grid<>(User.class, false);


    public UsersView(UserService userService){
        this.userService = userService;
        grid.addColumn(createUserRenderer()).setHeader("Employee")
                .setComparator(User::getUserName);
        grid.addColumn(User::getUserName).setHeader("Role")
                .setComparator(User::getUserName);
        grid.setItems(userService.getAll());
        add(grid);
    }

    private Renderer<User> createUserRenderer() {
        return new ComponentRenderer<>(user ->
                new HorizontalLayout(new Div(user.getUserName())));
    }
}
