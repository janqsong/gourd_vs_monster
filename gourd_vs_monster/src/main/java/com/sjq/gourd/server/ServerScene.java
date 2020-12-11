package com.sjq.gourd.server;

import com.sjq.gourd.constant.ImageUrl;
import javafx.fxml.FXML;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.HashMap;
import java.util.Map;

public class ServerScene {
    private HashMap<String, ImageView> gourdFamily = new HashMap<String, ImageView>();
    private HashMap<String, ImageView> monsterFamily = new HashMap<String, ImageView>();

    public ServerScene() {
        ImageUrl.initImageUrl();
        for(Map.Entry<String, String> entry : ImageUrl.gourdImageUrlMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            ImageView tempImageView = new ImageView();
            Image tempImage = new Image(value);
            tempImageView.setImage(tempImage);
            gourdFamily.put(key, tempImageView);
        }
    }

    public void setImageViewPos(String creatureType, String creatureId, double layoutX, double layoutY) {
        if(creatureType.equals("Gourd")) {
            ImageView tempImageView = gourdFamily.get(creatureId);
            tempImageView.setLayoutX(layoutX);
            tempImageView.setLayoutY(layoutY);
        } else if(creatureType.equals("Monster")) {
            ImageView tempImageView = monsterFamily.get(creatureId);
            tempImageView.setLayoutX(layoutX);
            tempImageView.setLayoutY(layoutY);
        }
    }

    public void startGame() {
        while(true) {
            try {
                Thread.sleep(5000);
                System.out.println("服务器正在沉睡");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
