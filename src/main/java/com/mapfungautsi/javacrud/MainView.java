
package com.mapfungautsi.javacrud;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route("")
public class MainView extends VerticalLayout {

    private final List<Contact> contacts = new ArrayList<>();
    private final Grid<Contact> grid = new Grid<>(Contact.class, false);

    private final TextField nameField = new TextField("Name");
    private final TextField emailField = new TextField("Email");

    private Contact selectedContact;

    public MainView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("My Contact Management System"));

        nameField.setRequired(true);
        emailField.setRequired(true);

        grid.addColumn(Contact::getName).setHeader("Name");
        grid.addColumn(Contact::getEmail).setHeader("Email");
        grid.setWidthFull();

        Button addButton = new Button("Add Contact", event -> addContact());
        Button updateButton = new Button("Update Contact", event -> updateContact());
        Button deleteButton = new Button("Delete Contact", event -> deleteContact());
        Button clearButton = new Button("Clear", event -> clearForm());

        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedContact = event.getValue();

            if (selectedContact != null) {
                nameField.setValue(selectedContact.getName());
                emailField.setValue(selectedContact.getEmail());
            }
        });

        HorizontalLayout form = new HorizontalLayout(
                nameField, emailField
        );
        form.setAlignItems(Alignment.END);

        HorizontalLayout buttons = new HorizontalLayout(
                addButton, updateButton, deleteButton, clearButton
        );

        add(form, buttons, grid);

        // Example records
        contacts.add(new Contact("Priveledge 73402", "73402@example.com"));
        contacts.add(new Contact("John Brown", "john@example.com"));

        refreshGrid();
    }

    private void addContact() {
        if (!validForm()) return;

        contacts.add(new Contact(
                nameField.getValue().trim(),
                emailField.getValue().trim()
        ));

        refreshGrid();
        clearForm();
        Notification.show("Contact added successfully!");
    }

    private void updateContact() {
        if (selectedContact == null) {
            Notification.show("Select a contact to update");
            return;
        }

        if (!validForm()) return;

        selectedContact.setName(nameField.getValue().trim());
        selectedContact.setEmail(emailField.getValue().trim());

        refreshGrid();
        clearForm();
        Notification.show("Contact updated successfully!");
    }

    private void deleteContact() {
        if (selectedContact == null) {
            Notification.show("Select a contact to delete");
            return;
        }

        contacts.remove(selectedContact);
        refreshGrid();
        clearForm();
        Notification.show("Contact deleted successfully!");
    }

    private boolean validForm() {
        if (nameField.getValue().isBlank()
                || emailField.getValue().isBlank()) {
            Notification.show("Please enter both name and email");
            return false;
        }
        return true;
    }

    private void refreshGrid() {
        grid.setItems(contacts);
    }

    private void clearForm() {
        selectedContact = null;
        grid.asSingleSelect().clear();
        nameField.clear();
        emailField.clear();
    }

    public static class Contact {
        private String name;
        private String email;

        public Contact() {
        }

        public Contact(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
