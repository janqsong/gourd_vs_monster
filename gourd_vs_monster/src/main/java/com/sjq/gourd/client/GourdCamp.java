package com.sjq.gourd.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.GourdClass;
import com.sjq.gourd.creature.MonsterClass;
import com.sjq.gourd.stage.SceneController;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GourdCamp extends Camp{

    public GourdCamp(SceneController sceneController,
                     DataInputStream in, DataOutputStream out) {
        super(sceneController, in, out);
    }

    public void initGame() {
        for(Map.Entry<Integer, String> entry : ImageUrl.gourdImageUrlMap.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();
            ImageView tempImageView = new ImageView();
            Image tempImage = new Image(value);
            tempImageView.setImage(tempImage);
            gourdFamily.get(key).addCreatureImageUrl(value);
            gourdFamily.get(key).addCreatureImageView(tempImageView);
        }
        for(Map.Entry<Integer, String> entry : ImageUrl.gourdSelectImageUrlMap.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();
            gourdFamily.get(key).addSelectCreatureImageUrl(value);
        }
        double layoutX = 160;
        for(GourdClass gourdMember : gourdFamily.values()) {
            ImageView tempImageView = gourdMember.getCreatureImageView();
            tempImageView.setFitWidth(80);
            tempImageView.setPreserveRatio(true);
            tempImageView.setLayoutX(layoutX);
            tempImageView.setLayoutY(576.0);
            layoutX += 100;
            tempImageView.setVisible(false);
            tempImageView.setDisable(true);
            sceneController.addImageView(tempImageView);
        }

        for(Map.Entry<Integer, String> entry : ImageUrl.monsterImageUrlMap.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();
            ImageView tempImageView = new ImageView();
            Image tempImage = new Image(value);
            tempImageView.setImage(tempImage);
            monsterFamily.get(key).addCreatureImageUrl(value);
            monsterFamily.get(key).addCreatureImageView(tempImageView);
        }
        for(Map.Entry<Integer, String> entry : ImageUrl.monsterSelectImageUrlMap.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();
            monsterFamily.get(key).addSelectCreatureImageUrl(value);
        }
        for(MonsterClass gourdMember : monsterFamily.values()) {
            ImageView tempImageView = gourdMember.getCreatureImageView();
            tempImageView.setFitWidth(80);
            tempImageView.setPreserveRatio(true);
            tempImageView.setVisible(false);
            tempImageView.setDisable(true);
            sceneController.addImageView(tempImageView);
        }
    }

    public void startGame() {
        initGame();
        sceneController.initGameSceneController(in, out, "Gourd", gourdFamily, monsterFamily);
        sceneController.gourdStartGame();
    }
}