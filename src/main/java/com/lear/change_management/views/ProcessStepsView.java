package com.lear.change_management.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route(value = "/projects/processsteps/")
public class ProcessStepsView extends Div implements HasUrlParameter<String> {

    private Long projectId = 0L;

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        this.projectId = Long.getLong(s);
    }
}
