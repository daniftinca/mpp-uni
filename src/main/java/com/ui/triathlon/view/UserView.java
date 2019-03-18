package com.ui.triathlon.view;

import com.triathlon.controller.UserController;
import com.triathlon.domain.User;
import com.triathlon.repository.RepositoryException;
import com.ui.triathlon.ui_utils.Utils;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class UserView {
    BorderPane pane;
    TextField userIdText, usernameBox, nameBox, passwordBox;
    UserController controller;

    private TableView<User> table = new TableView<>();


    public UserView(UserController contr) {
        this.controller = contr;
        initView();

    }

    static void showErrorMessage(String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setRight(createUser());
        pane.setCenter(createTable());
    }

    public BorderPane getView() {

        return pane;
    }

    private StackPane createTable() {
        StackPane pane = new StackPane();
        initUserView();
        pane.getChildren().add(table);
        return pane;
    }

    private void initUserView() {
        TableColumn<User, Long> idCol = new TableColumn<>("Id");
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        TableColumn<User, String> nameCol = new TableColumn<>("Name");

        table.getColumns().addAll(idCol, usernameCol, nameCol);

        //stabilirea valorilor asociate unei celule
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID")); //
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.setItems(controller.getUsersModel());

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Listen for selection changes and show the SortingTask details when changed.
        table.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldvalue, newValue) -> showUserDetails(newValue));

    }

    private void showUserDetails(User value) {
        if (value == null)
            clearFields();
        else {
            userIdText.setText("" + value.getID());
            usernameBox.setText("" + value.getUsername());
            nameBox.setText("" + value.getName());
            passwordBox.setText("" + value.getPassword());
        }
    }

    protected GridPane createUser() {
        GridPane grid = Utils.initWindow();

        Label userID = new Label("Id:");
        grid.add(userID, 0, 1);
        userIdText = new TextField();
        grid.add(userIdText, 1, 1);

        Label usrLabel = new Label("Username:");
        grid.add(usrLabel, 0, 2);
        usernameBox = new TextField();
        grid.add(usernameBox, 1, 2);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 3);
        passwordBox = new PasswordField();
        grid.add(passwordBox, 1, 3);

        Label nameLabel = new Label("Name:");
        grid.add(nameLabel, 0, 4);
        nameBox = new TextField();
        grid.add(nameBox, 1, 4);


        Button addUser = new Button("Add");
        Button deleteUser = new Button("Delete");
        Button updateUser = new Button("Update");
        Button cancel = new Button("Cancel");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        addUser.setOnAction(x -> addButton());
        deleteUser.setOnAction(x -> handleDelete());
        updateUser.setOnAction(x -> handleUpdate());
        cancel.setOnAction(x -> cancelButton());
        hbBtn.getChildren().addAll(addUser, deleteUser, updateUser, cancel);
        grid.add(hbBtn, 0, 6, 2, 1);


        return grid;
    }

    private void addButton() {
        String id = userIdText.getText();
        String username = usernameBox.getText();
        String name = nameBox.getText();
        String password = passwordBox.getText();
        try {
            Long idVal = Long.parseLong(id);
            controller.addUser(idVal, username, password, name);
            refreshView();

        } catch (NumberFormatException ex) {
            showErrorMessage("Id-ul  trebuie sa fie numar intreg! " + ex.getMessage());
        } catch (RepositoryException ex) {
            showErrorMessage("Eroare la adaugare: " + ex.getMessage());
        }
    }

    private void refreshView() {
        clearFields();

        table.setItems(controller.getUsersModel());
    }

    private void handleDelete() {
        int index = table.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showErrorMessage("Eroare la stergere: trebuie sa selectati un user");
            return;
        }
        User user = table.getSelectionModel().getSelectedItem();
        controller.deleteUser(user);
        refreshView();
    }

    private void handleUpdate() {
        System.out.println("S-a apasat update");
        int index = table.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showErrorMessage("Trebuie sa selectati un user!!!");
            return;
        }
        User oldUser = table.getSelectionModel().getSelectedItem();
        String id = userIdText.getText();
        String username = usernameBox.getText();
        String name = nameBox.getText();
        String password = passwordBox.getText();
        try {
            Long idVal = Long.parseLong(id);

            controller.updateUser(oldUser, idVal, username, password, name);
            refreshView();

        } catch (NumberFormatException ex) {
            showErrorMessage("Id-ul trebuie sa fie numar intreg! " + ex.getMessage());
        } catch (RepositoryException ex) {
            showErrorMessage("Eroare la update: " + ex.getMessage());
        }


    }

    private void cancelButton() {
        table.getSelectionModel().clearSelection();
        clearFields();
    }

    private void clearFields() {
        userIdText.setText("");
        usernameBox.setText("");
        nameBox.setText("");
        passwordBox.setText("");
    }
}
