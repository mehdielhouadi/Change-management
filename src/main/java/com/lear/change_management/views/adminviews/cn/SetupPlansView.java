package com.lear.change_management.views.adminviews.cn;

import com.lear.change_management.entities.SetupPlan;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.Map;

@Route("/setup")
@RolesAllowed("ADMIN")
public class SetupPlansView extends VerticalLayout implements HasUrlParameter<String> {

    private Long CnId = 0L;
    Grid<SetupPlan> grid = new Grid<>(SetupPlan.class, false);

    public SetupPlansView() {

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String s) {
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters
                .getParameters();
        List<String> CnIdList = parametersMap.get("CNid");
        String CnIdString = CnIdList.getFirst();
        add(new Div(new H1("setup plan" + CnIdString)));
    }
}
