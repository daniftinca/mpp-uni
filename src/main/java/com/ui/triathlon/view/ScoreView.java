package com.ui.triathlon.view;

import com.triathlon.controller.ScoreController;
import com.triathlon.controller.UserSession;
import com.triathlon.domain.Participant;
import com.triathlon.domain.Proba;
import com.triathlon.domain.Score;
import com.triathlon.repository.RepositoryException;
import com.ui.triathlon.ui_utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ScoreView {

    BorderPane pane;
    TextField idBox, participantName, probaName, scoreValue,
            participantIDText, probaIdtext, probaSelectName;

    Boolean canEdit = true;
    Participant selectedParticipant;
    Proba selectedProba;
    ScoreController controller;


    private TableView<Score> table = new TableView<>();

    public ScoreView(UserSession session, ScoreController controller) {
        this.controller = controller;
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setRight(createScore());
        pane.setCenter(createTable());
        pane.setLeft(controlButtons());
    }

    public BorderPane getView() {
        return pane;
    }

    private StackPane createTable() {
        StackPane pane = new StackPane();
        initScoreView();
        pane.getChildren().add(table);
        return pane;
    }

    private GridPane controlButtons() {
        GridPane grid = Utils.initWindow();

        Button yourScores = new Button("Vezi scorurile tale");

        grid.add(yourScores, 0, 0);

        Label probaNameLabel = new Label("ID Proba: ");
        grid.add(probaNameLabel, 0, 1);
        probaSelectName = new TextField();
        grid.add(probaSelectName, 0, 2);

        Button executeGetStats = new Button("Vezi scorurile pentru proba");
        grid.add(executeGetStats, 0, 3);

        yourScores.setOnAction(event -> handleRedoTable());
        executeGetStats.setOnAction(event -> handleStats());

        return grid;
    }

    private void handleRedoTable() {
        controller.populateListForProbe();
        table.setItems(controller.getScoresModel());
        canEdit = true;
    }

    private void handleStats() {
        Long probaName = Long.parseLong(probaSelectName.getText());
        controller.populateListForProba(probaName);

        table.setItems(controller.getScoresModel());
        canEdit = false;
    }


    private void initScoreView() {
        TableColumn<Score, Long> idCol = new TableColumn<>("Id");
        TableColumn<Score, String> participantCol = new TableColumn<>("Participant");
        TableColumn<Score, String> probaCol = new TableColumn<>("Proba");
        TableColumn<Score, Integer> scoreCol = new TableColumn<>("Scor");

        table.getColumns().addAll(idCol, participantCol, probaCol, scoreCol);

        //stabilirea valorilor asociate unei celule
        idCol.setCellValueFactory(new PropertyValueFactory<>("Id")); //
        participantCol.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(cellData.getValue().getParticipant().getName()));
        probaCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProba().getName()));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

        table.setItems(controller.getScoresModel());

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Listen for selection changes and show the SortingTask details when changed.
        table.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldvalue, newValue) -> showScoreDetails(newValue));

    }

    private void showScoreDetails(Score value) {
        if (value == null)
            clearFields();
        else {
            idBox.setText("" + value.getId());
            //participantName.setText("" + value.getParticipant().getName());
            participantIDText.setText("" + value.getParticipant().getId());
            //probaName.setText("" + value.getProba().getName());
            probaIdtext.setText("" + value.getProba().getId());
            scoreValue.setText("" + value.getScore());
            selectedParticipant = value.getParticipant();
            selectedProba = value.getProba();
            probaSelectName.setText("" + value.getProba().getId());
        }
    }

    protected GridPane createScore() {
        GridPane grid = Utils.initWindow();

        Label scoreID = new Label("Id:");
        grid.add(scoreID, 0, 1);
        idBox = new TextField();
        grid.add(idBox, 1, 1);

        Label participantID = new Label("Id Participant:");
        grid.add(participantID, 0, 2);
        participantIDText = new TextField();
        grid.add(participantIDText, 1, 2);

        Label probaID = new Label("Id Proba:");
        grid.add(probaID, 0, 3);
        probaIdtext = new TextField();
        grid.add(probaIdtext, 1, 3);

        Label scoreVal = new Label("Scor:");
        grid.add(scoreVal, 0, 4);
        scoreValue = new TextField();
        grid.add(scoreValue, 1, 4);

        Button addButton = new Button("Add");
        Button deleteButton = new Button("Delete");
        Button updateButton = new Button("Update");
        Button cancel = new Button("Cancel");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        addButton.setOnAction(x -> addButton());
        deleteButton.setOnAction(x -> handleDelete());
        updateButton.setOnAction(x -> handleUpdate());
        cancel.setOnAction(x -> cancelButton());
        hbBtn.getChildren().addAll(addButton, deleteButton, updateButton, cancel);
        grid.add(hbBtn, 0, 6, 2, 1);


        return grid;

    }

    private void addButton() {
        if (canEdit) {
            String id = idBox.getText();

            try {
                Long probaID = Long.parseLong(probaIdtext.getText());
                Proba proba = controller.getProbaByID(probaID);

                Long participantID = Long.parseLong(participantIDText.getText());
                Participant participant = controller.getParticipantByID(participantID);

                Integer newScore = Integer.parseInt(scoreValue.getText());

                Long idVal = Long.parseLong(id);
                controller.addScore(idVal, participant, proba, newScore);
                refreshView();

            } catch (NumberFormatException ex) {
                Utils.showDialog("Id-urile  trebuie sa fie numare intregi! " + ex.getMessage(), "Error", Alert.AlertType.ERROR);
            } catch (RepositoryException ex) {
                Utils.showDialog("Eroare la adaugare: " + ex.getMessage(), "Error", Alert.AlertType.ERROR);
            } catch (Exception ex) {
                Utils.showDialog(ex.getMessage(), "Eroare", Alert.AlertType.ERROR);
            }
        } else {
            Utils.showDialog("Nu poti edita aici", "Eroare", Alert.AlertType.ERROR);
        }
    }

    private void handleDelete() {
        if (canEdit) {
            int index = table.getSelectionModel().getSelectedIndex();
            if (index < 0) {
                Utils.showDialog("Eroare la stergere: trebuie sa selectati un user", "Error", Alert.AlertType.ERROR);
                return;
            }
            Score score = table.getSelectionModel().getSelectedItem();
            controller.deleteScore(score);
            refreshView();
        } else {
            Utils.showDialog("Nu poti edita aici", "Eroare", Alert.AlertType.ERROR);
        }
    }


    private void handleUpdate() {
        if (canEdit) {
            System.out.println("S-a apasat update");
            int index = table.getSelectionModel().getSelectedIndex();
            if (index < 0) {
                Utils.showDialog("Trebuie sa selectati un user!!!", "Error", Alert.AlertType.ERROR);
                return;
            }
            try {
                String id = idBox.getText();
                Score oldScore = table.getSelectionModel().getSelectedItem();
                Long probaID = Long.parseLong(probaIdtext.getText());
                Proba proba = controller.getProbaByID(probaID);

                Long participantID = Long.parseLong(participantIDText.getText());
                Participant participant = controller.getParticipantByID(participantID);

                Integer newScore = Integer.parseInt(scoreValue.getText());

                Long idVal = Long.parseLong(id);


                controller.updateScore(oldScore, idVal, participant, proba, newScore);
                refreshView();

            } catch (NumberFormatException ex) {
                Utils.showDialog("Id-ul trebuie sa fie numar intreg! " + ex.getMessage(), "Error", Alert.AlertType.ERROR);
            } catch (RepositoryException ex) {
                Utils.showDialog("Eroare la update: " + ex.getMessage(), "Error", Alert.AlertType.ERROR);
            } catch (Exception ex) {
                Utils.showDialog(ex.getMessage(), "Error", Alert.AlertType.ERROR);

            }
        } else {
            Utils.showDialog("Nu poti edita aici", "Eroare", Alert.AlertType.ERROR);
        }


    }

    private void refreshView() {
        clearFields();

        table.setItems(controller.getScoresModel());
    }


    private void cancelButton() {
        table.getSelectionModel().clearSelection();
        clearFields();
    }

    private void clearFields() {
        idBox.setText("");
        scoreValue.setText("");
        participantIDText.setText("");
        probaIdtext.setText("");
        selectedProba = null;
        selectedParticipant = null;
    }


}
