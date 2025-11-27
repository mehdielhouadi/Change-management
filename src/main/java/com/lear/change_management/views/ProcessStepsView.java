package com.lear.change_management.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Route(value = "/projects/processsteps/")
public class ProcessStepsView extends VerticalLayout implements HasUrlParameter<String> {

    private Long projectId = 0L;

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String s) {
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters
                .getParameters();
        List<String> projectIdList = parametersMap.get(s);
        String projectIdString = projectIdList.get(0);
        try {
            projectId = Long.valueOf(projectIdString);
        } catch (NumberFormatException exception) {
            projectId = 0L;
        }
    }



}
