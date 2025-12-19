package com.lear.change_management.views.userviews;

import com.lear.change_management.views.adminviews.ui.NestedLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "CNs", layout = NestedLayout.class)
@Menu(title = "CNs", order = 4, icon = "vaadin:bullets")
@RolesAllowed("ENGINEERING")
public class SetupsView {

}
