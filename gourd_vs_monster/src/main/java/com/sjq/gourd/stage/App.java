package com.sjq.gourd.stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(this.getClass().getResource("SceneController.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1200, 700);
            //scene.getStylesheets().addAll(this.getClass().getResource("/style.css").toExternalForm());
            primaryStage.setTitle("葫芦娃大战妖精");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

