package com.sjq.gourd.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sjq.gourd.constant.Constant;
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
        double layoutY = 10;
        for(GourdClass gourdMember : gourdFamily.values()) {
            gourdMember.setCreatureImagePos(40, layoutY);
            layoutY += 80;
        }
    }

    public void startGame() {
        initGame();
        sceneController.initGameSceneController(in, out, "Gourd", gourdFamily, monsterFamily);
        sceneController.gourdStartGame();
    }
}