package com.ui.triathlon;

import com.triathlon.controller.UserController;
import com.triathlon.repository.UserRepository;
import com.triathlon.service.UserService;
import com.ui.triathlon.view.UserView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class UserMain extends Application {
    static BorderPane getView() {
        Properties dbProps = new Properties();
        try {
            dbProps.load(new FileReader("bd.config"));
            UserRepository repo = new UserRepository(dbProps);

            UserService service = new UserService(repo);
            UserController contr = new UserController(service);
            UserView view = new UserView(contr);
            return view.getView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("SortingTasks Application");
        BorderPane pane = getView();
        Scene scene = new Scene(pane, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
