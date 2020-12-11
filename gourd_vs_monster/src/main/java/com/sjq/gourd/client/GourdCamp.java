package com.sjq.gourd.client;

import java.util.HashMap;
import java.util.Map;

import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.stage.SceneController;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GourdCamp {
    private SceneController sceneController;
    private HashMap<String, ImageView> gourdFamily = new HashMap<String, ImageView>();
    private HashMap<String, ImageView> monsterFamily = new HashMap<String, ImageView>();
    
    public GourdCamp(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    public void initGame() {
        ImageUrl.initImageUrl();
        for(Map.Entry<String, String> entry : ImageUrl.gourdImageUrlMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            ImageView tempImageView = new ImageView();
            Image tempImage = new Image(value);
            tempImageView.setImage(tempImage);
            gourdFamily.put(key, tempImageView);
        }
        double layoutX = 160;
        for(ImageView tempImageView : gourdFamily.values()) {
            tempImageView.setFitWidth(80);
            tempImageView.setPreserveRatio(true);
            tempImageView.setLayoutX(layoutX);
            tempImageView.setLayoutY(576.0);
            layoutX += 100;
            tempImageView.setVisible(true);
            sceneController.addImageView(tempImageView);
        }
    }

    public void startGame() {
        sceneController.startGame();
    }
}