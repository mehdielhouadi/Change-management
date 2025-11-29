package com.lear.change_management.views;

import com.lear.change_management.services.PasswordResetService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "reset-password", autoLayout = false)
@AnonymousAllowed
public class ResetPasswordView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private PasswordResetService passwordResetService;

    private String token;

    public ResetPasswordView() {
        setSizeFull(); // full height + width of the page
        setJustifyContentMode(JustifyContentMode.CENTER); // vertical centering
        setAlignItems(Alignment.CENTER); // horizontal centering
        setPadding(true);
        setSpacing(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Location location = event.getLocation();
        var query = location.getQueryParameters();

        if (!query.getParameters().containsKey("token")) {
            event.forwardTo("error");
            return;
        }

        token = query.getParameters().get("token").get(0);

        PasswordField newPass = new PasswordField("New password");
        newPass.setWidth("400px");

        PasswordField confirmPass = new PasswordField("Confirm password");
        confirmPass.setWidth("400px");

        Button resetBtn = new Button("Reset Password", e -> {
            if (!newPass.getValue().equals(confirmPass.getValue())) {
                Notification.show("Passwords do not match");
                return;
            }

            boolean ok = passwordResetService.resetPassword(token, newPass.getValue());
            if (ok) {
                Notification.show("Password changed successfully!");
                event.getUI().navigate("login");
            } else {
                Notification.show("Invalid or expired token");
            }
        });

        add(newPass, confirmPass, resetBtn);
    }
}
