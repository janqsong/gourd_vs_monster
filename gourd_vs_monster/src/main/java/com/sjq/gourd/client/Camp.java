package com.sjq.gourd.client;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.CreatureClass;
import com.sjq.gourd.creature.GourdClass;
import com.sjq.gourd.creature.MonsterClass;
import com.sjq.gourd.stage.SceneController;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.json.*;

public class Camp {
    protected SceneController sceneController;
    protected HashMap<Integer, CreatureClass> gourdFamily = new HashMap<Integer, CreatureClass>();
    protected HashMap<Integer, CreatureClass> monsterFamily = new HashMap<Integer, CreatureClass>();
    protected DataInputStream in;
    protected DataOutputStream out;

    public Camp(SceneController sceneController,
                DataInputStream in, DataOutputStream out) {
        this.sceneController = sceneController;
        this.in = in;
        this.out = out;

        JSONObject gourdJSONObject = new JSONObject(getJsonContentText("GourdInformation.json"));
        JSONArray gourdInfoArray = (JSONArray) gourdJSONObject.get("gourdInfo");

        JSONObject monsterJSONObject = new JSONObject(getJsonContentText("MonsterInformation.json"));
        JSONArray monsterInfoArray = (JSONArray) monsterJSONObject.get("monsterInfo");

        ImageUrl.initImageUrl();
        //TODO 初始化
        for (int i = 0; i < gourdInfoArray.length(); i++) {
            JSONObject gourdObject = (JSONObject) gourdInfoArray.get(i);
            int creatureId = gourdObject.getInt("creatureId");
            String creatureName = gourdObject.getString("creatureName");
            int baseHealth = gourdObject.getInt("baseHealth");
            int baseMagic = gourdObject.getInt("baseMagic");
            int baseAttack = gourdObject.getInt("baseAttack");
            int baseDefense = gourdObject.getInt("baseDefense");
            int baseAttackSpeed = gourdObject.getInt("baseAttackSpeed");
            int baseMoveSpeed = gourdObject.getInt("baseMoveSpeed");
            double shootRange = gourdObject.getDouble("shootRange");
            int faceDirection = gourdObject.getInt("faceDirection");
            double imageWidth = gourdObject.getDouble("imageWidth");
            Image gourdLeftImage = ImageUrl.gourdLeftImageMap.get(creatureId);
            Image gourdLeftSelectImage = ImageUrl.gourdLeftSelectImageMap.get(creatureId);
            Image gourdRightImage = ImageUrl.gourdRightImageMap.get(creatureId);
            Image gourdRightSelectImage = ImageUrl.gourdRightSelectImageMap.get(creatureId);
            GourdClass gourdMember = new GourdClass(in, out, Constant.CampType.GOURD, creatureId, creatureName,
                    baseHealth, baseMagic, baseAttack, baseDefense, baseAttackSpeed, baseMoveSpeed, shootRange, faceDirection,
                    imageWidth, gourdLeftImage, gourdLeftSelectImage, gourdRightImage, gourdRightSelectImage);
            gourdMember.setCreatureImageView();
            sceneController.addProgressBarToMapPane(gourdMember.getHealthProgressBar());
            sceneController.addProgressBarToMapPane(gourdMember.getMagicProgressBar());

            gourdFamily.put(creatureId, gourdMember);

        }

        for (int i = 0; i < monsterInfoArray.length(); i++) {
            JSONObject monsterObject = (JSONObject) monsterInfoArray.get(i);
            int creatureId = monsterObject.getInt("creatureId");
            String creatureName = monsterObject.getString("creatureName");
            int baseHealth = monsterObject.getInt("baseHealth");
            int baseMagic = monsterObject.getInt("baseMagic");
            int baseAttack = monsterObject.getInt("baseAttack");
            int baseDefense = monsterObject.getInt("baseDefense");
            int baseAttackSpeed = monsterObject.getInt("baseAttackSpeed");
            int baseMoveSpeed = monsterObject.getInt("baseMoveSpeed");
            double shootRange = monsterObject.getDouble("shootRange");
            int faceDirection = monsterObject.getInt("faceDirection");
            double imageWidth = monsterObject.getDouble("imageWidth");
            Image monsterLeftImage = ImageUrl.monsterLeftImageMap.get(creatureId);
            Image monsterLeftSelectImage = ImageUrl.monsterLeftSelectImageMap.get(creatureId);
            Image monsterRightImage = ImageUrl.monsterRightImageMap.get(creatureId);
            Image monsterRightSelectImage = ImageUrl.monsterRightSelectImageMap.get(creatureId);
            MonsterClass monsterMember = new MonsterClass(in, out, Constant.CampType.GOURD, creatureId, creatureName,
                    baseHealth, baseMagic, baseAttack, baseDefense, baseAttackSpeed, baseMoveSpeed, shootRange, faceDirection,
                    imageWidth, monsterLeftImage, monsterLeftSelectImage, monsterRightImage, monsterRightSelectImage);
            monsterMember.setCreatureImageView();

            sceneController.addProgressBarToMapPane(monsterMember.getHealthProgressBar());
            sceneController.addProgressBarToMapPane(monsterMember.getMagicProgressBar());
            monsterFamily.put(creatureId, monsterMember);
        }
    }

    public String getJsonContentText(String path) {
        StringBuilder jsonContent = new StringBuilder();
        BufferedReader reader = null;
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream(path);
            assert in != null;
            reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                jsonContent.append(tempString);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonContent.toString();
    }
}