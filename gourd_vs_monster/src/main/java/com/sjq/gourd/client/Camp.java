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
import com.sjq.gourd.server.MsgController;
import com.sjq.gourd.stage.SceneController;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Camp {
    protected SceneController sceneController;
    protected HashMap<Integer, GourdClass> gourdFamily = new HashMap<Integer, GourdClass>();
    protected HashMap<Integer, MonsterClass> monsterFamily = new HashMap<Integer, MonsterClass>();
    protected DataInputStream in;
    protected DataOutputStream out;

    public Camp(SceneController sceneController,
                     DataInputStream in, DataOutputStream out) {
        this.sceneController = sceneController;
        this.in = in;
        this.out = out;
        gourdFamily.put(CreatureId.FIRST_GOURD_ID, new GourdClass(CreatureId.FIRST_GOURD_NAME));
        gourdFamily.put(CreatureId.SECOND_GOURD_ID, new GourdClass(CreatureId.SECOND_GOURD_NAME));
        gourdFamily.put(CreatureId.THIRD_GOURD_ID, new GourdClass(CreatureId.THIRD_GOURD_NAME));
        gourdFamily.put(CreatureId.FOURTH_GOURD_ID,new GourdClass(CreatureId.FOURTH_GOURD_NAME));
        gourdFamily.put(CreatureId.FIFTH_GOURD_ID, new GourdClass(CreatureId.FIFTH_GOURD_NAME));
        gourdFamily.put(CreatureId.SIXTH_GOURD_ID, new GourdClass(CreatureId.SIXTH_GOURD_NAME));
        gourdFamily.put(CreatureId.SEVENTH_GOURD_ID, new GourdClass(CreatureId.SEVENTH_GOURD_NAME));
        gourdFamily.put(CreatureId.PANGOLIN_ID, new GourdClass(CreatureId.PANGOLIN_NAME));
        gourdFamily.put(CreatureId.GRANDPA_ID, new GourdClass(CreatureId.GRANDPA_NAME));
        ImageUrl.initImageUrl();
        for(Map.Entry<Integer, String> entry : ImageUrl.gourdImageUrlMap.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();
            gourdFamily.get(key).addCreatureImageUrl(value);
            gourdFamily.get(key).setCreatureImageView();
        }

        monsterFamily.put(CreatureId.SNAKE_GOBLIN_ID, new MonsterClass(CreatureId.SNAKE_GOBLIN_NAME));
        monsterFamily.put(CreatureId.SCORPION_GOBLIN_ID, new MonsterClass(CreatureId.SCORPION_GOBLIN_NAME));
        monsterFamily.put(CreatureId.MONSTER1_ID, new MonsterClass(CreatureId.MONSTER1_NAME));
        monsterFamily.put(CreatureId.MONSTER2_ID, new MonsterClass(CreatureId.MONSTER2_NAME));
        monsterFamily.put(CreatureId.MONSTER3_ID, new MonsterClass(CreatureId.MONSTER3_NAME));
        monsterFamily.put(CreatureId.MONSTER4_ID, new MonsterClass(CreatureId.MONSTER4_NAME));
        monsterFamily.put(CreatureId.MONSTER5_ID, new MonsterClass(CreatureId.MONSTER5_NAME));
        monsterFamily.put(CreatureId.MONSTER6_ID, new MonsterClass(CreatureId.MONSTER6_NAME));
        monsterFamily.put(CreatureId.MONSTER7_ID, new MonsterClass(CreatureId.MONSTER7_NAME));
        for(Map.Entry<Integer, String> entry : ImageUrl.monsterImageUrlMap.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();
            monsterFamily.get(key).addCreatureImageUrl(value);
            monsterFamily.get(key).setCreatureImageView();
        }
    }

}