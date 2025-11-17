package com.lear.change_management.views.ui;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;

@ParentLayout(MainLayout.class)
public class NestedLayout extends Div implements RouterLayout {

    private final Div content;

    public NestedLayout() {
        addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Gap.SMALL, LumoUtility.Padding.MEDIUM,
                LumoUtility.BoxSizing.BORDER, LumoUtility.Height.FULL);
        content = new Div();
        content.addClassNames(LumoUtility.Border.ALL, LumoUtility.Background.BASE);
        content.setSizeFull();
        add(content);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        this.content.getElement().appendChild(content.getElement());
    }
}
