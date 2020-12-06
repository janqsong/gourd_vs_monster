package com.sjq.gourd.stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SceneController {

    @FXML
    private Button connectButton;

    @FXML
    void about_us(ActionEvent event) {
        System.out.println("about us");
    }

    @FXML
    void game_discription(ActionEvent event) {
        System.out.println("game discription");
    }

    @FXML
    void local_playback(ActionEvent event) {
        System.out.println("local playback");
    }

    @FXML
    void start_network_fight(ActionEvent event) {
        System.out.println("start network fight");
    }

}
