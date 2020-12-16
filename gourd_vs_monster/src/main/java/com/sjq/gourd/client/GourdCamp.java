package com.sjq.gourd.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.sjq.gourd.creature.CreatureClass;
import com.sjq.gourd.creature.GourdClass;
import com.sjq.gourd.creature.MonsterClass;
import com.sjq.gourd.stage.SceneController;


public class GourdCamp extends Camp{

    public GourdCamp(SceneController sceneController,
                     DataInputStream in, DataOutputStream out) {
        super(sceneController, in, out);
    }

    public void initGame() {
        double layoutY = 10;
        for(CreatureClass gourdMember : gourdFamily.values()) {
            gourdMember.setCreatureImagePos(40, layoutY);
            layoutY += 80;
            sceneController.addImageViewToFightMapPane(gourdMember.getCreatureImageView());
        }
        for(CreatureClass monsterMember : monsterFamily.values()) {
            monsterMember.setCreatureImagePos(500, 100);
            monsterMember.getCreatureImageView().setVisible(true);
            monsterMember.getCreatureImageView().setVisible(false);
            sceneController.addImageViewToMapPane(monsterMember.getCreatureImageView());
        }
    }

    public void startGame() {
        initGame();
        sceneController.initGameSceneController(in, out, "Gourd", gourdFamily, monsterFamily);
        sceneController.gourdStartFight();
    }
}