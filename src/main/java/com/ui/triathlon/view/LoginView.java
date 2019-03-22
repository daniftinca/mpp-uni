package com.ui.triathlon.view;

import com.triathlon.controller.AuthenticationController;
import com.triathlon.controller.GeneralController;
import com.triathlon.controller.UserSession;
import com.triathlon.repository.ParticipantRepository;
import com.triathlon.repository.ProbaRepository;
import com.triathlon.repository.ScoreRepository;
import com.triathlon.service.ParticipantService;
import com.triathlon.service.ProbaService;
import com.triathlon.service.ScorService;
import com.ui.triathlon.ui_utils.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class LoginView {
    BorderPane pane;
    TextField usernameBox, passwordBox;

    AuthenticationController controller;

    private static final Logger logger = LogManager.getLogger();

    public LoginView(AuthenticationController controller) {
        this.controller = controller;
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setCenter(userLogin());
    }

    public BorderPane getView() {
        return pane;
    }

    public GridPane userLogin() {
        GridPane grid = Utils.initWindow("Login");

        Label usrLabel = new Label("Username:");
        grid.add(usrLabel, 0, 2);
        usernameBox = new TextField();
        grid.add(usernameBox, 1, 2);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 3);
        passwordBox = new PasswordField();
        grid.add(passwordBox, 1, 3);

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                login(event);

            }
        });
        hbBtn.getChildren().addAll(loginBtn);
        grid.add(hbBtn, 0, 6, 2, 1);

        return grid;
    }

    private void login(ActionEvent event) {
        String username = usernameBox.getText();
        String password = passwordBox.getText();
        if (controller.login(username, password)) {
            //Utils.showDialog("Login reusit","Hello", Alert.AlertType.INFORMATION);

            Properties dbProps = new Properties();
            try {
                dbProps.load(new FileReader("bd.config"));
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Failed to load dataabse");
            }

            ScoreRepository scoreRepository = new ScoreRepository(dbProps);
            ScorService scorService = new ScorService(scoreRepository);
            ProbaRepository probaRepository = new ProbaRepository(dbProps);
            ProbaService probaService = new ProbaService(probaRepository);
            ParticipantRepository participantRepository = new ParticipantRepository(dbProps);
            ParticipantService participantService = new ParticipantService(participantRepository);
            GeneralController generalController = new GeneralController(scorService, probaService, participantService);

            Pane root = new GeneralView(UserSession.getInstace(username), generalController).getView();
            Stage stage = new Stage();
            stage.setTitle("Scoruri");
            stage.setScene(new Scene(root, 850, 450));
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node) (event.getSource())).getScene().getWindow().hide();

        } else {
            Utils.showDialog("Login nereusit", "Eroare", Alert.AlertType.ERROR);
        }

    }


}
