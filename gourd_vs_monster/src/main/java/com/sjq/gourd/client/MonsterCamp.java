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

public class MonsterCamp extends Camp{

    public MonsterCamp(SceneController sceneController,
                     DataInputStream in, DataOutputStream out) {
        super(sceneController, in, out);
    }

    public void initGame() {
        double layoutX = 160;
        for(MonsterClass monsterMember : monsterFamily.values()) {
            monsterMember.setCreatureImagePos(layoutX, Constant.FIGHT_PANE_HEIGHT - 100);
            layoutX += 100;
        }
    }

    public void startGame() {
        initGame();
        sceneController.initGameSceneController(in, out, "Monster", gourdFamily, monsterFamily);
        sceneController.monsterStartGame();
    }
}