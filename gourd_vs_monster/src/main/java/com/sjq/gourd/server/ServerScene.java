package com.sjq.gourd.server;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.GourdClass;
import com.sjq.gourd.creature.MonsterClass;
import com.sjq.gourd.protocol.Msg;
import com.sjq.gourd.protocol.NoParseMsg;
import com.sjq.gourd.protocol.PositionNotifyMsg;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ServerScene {
    private HashMap<Integer, GourdClass> gourdFamily = new HashMap<Integer, GourdClass>();
    private HashMap<Integer, MonsterClass> monsterFamily = new HashMap<Integer, MonsterClass>();
    private DataInputStream inGourd;
    private DataOutputStream outGourd;
    private DataInputStream inMonster;
    private DataOutputStream outMonster;
    private MsgController msgController;

    boolean gourdFinishFlag = false;
    boolean monsterFinishFlag = false;

    public ServerScene(DataInputStream inGourd, DataOutputStream outGourd,
                       DataInputStream inMonster, DataOutputStream outMonster) {
        this.inGourd = inGourd;
        this.outGourd = outGourd;
        this.inMonster = inMonster;
        this.outMonster = outMonster;
        initScene();
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

    public void initScene() {
        JSONObject gourdJSONObject = new JSONObject(getJsonContentText("GourdInformation.json"));
        JSONArray gourdInfoArray = (JSONArray) gourdJSONObject.get("gourdInfo");

        JSONObject monsterJSONObject = new JSONObject(getJsonContentText("MonsterInformation.json"));
        JSONArray monsterInfoArray = (JSONArray) monsterJSONObject.get("monsterInfo");

        ImageUrl.initImageUrl();
        for(int i = 0; i < gourdInfoArray.length(); i++) {
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
            Image gourdLeftImage = ImageUrl.gourdLeftImageMap.get(creatureId);
            Image gourdLeftSelectImage = ImageUrl.gourdLeftSelectImageMap.get(creatureId);
            Image gourdRightImage = ImageUrl.gourdRightImageMap.get(creatureId);
            Image gourdRightSelectImage = ImageUrl.gourdRightSelectImageMap.get(creatureId);
            GourdClass gourdMember = new GourdClass(inGourd, outGourd, Constant.CampType.GOURD, creatureId, creatureName,
                    baseHealth, baseMagic, baseAttack, baseDefense, baseAttackSpeed, baseMoveSpeed, shootRange, faceDirection,
                    gourdLeftImage, gourdLeftSelectImage, gourdRightImage, gourdRightSelectImage);
            gourdMember.setCreatureImageView();
            gourdFamily.put(creatureId, gourdMember);

        }

        for(int i = 0; i < monsterInfoArray.length(); i++) {
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
            Image monsterLeftImage = ImageUrl.monsterLeftImageMap.get(creatureId);
            Image monsterLeftSelectImage = ImageUrl.monsterLeftSelectImageMap.get(creatureId);
            Image monsterRightImage = ImageUrl.monsterRightImageMap.get(creatureId);
            Image monsterRightSelectImage = ImageUrl.monsterRightSelectImageMap.get(creatureId);
            MonsterClass monsterMember = new MonsterClass(inMonster, outMonster, Constant.CampType.MONSTER, creatureId, creatureName,
                    baseHealth, baseMagic, baseAttack, baseDefense, baseAttackSpeed, baseMoveSpeed, shootRange, faceDirection,
                    monsterLeftImage, monsterLeftSelectImage, monsterRightImage, monsterRightSelectImage);
            monsterMember.setCreatureImageView();
            monsterFamily.put(creatureId, monsterMember);
        }
        msgController = new MsgController(gourdFamily, monsterFamily);
    }

    public void startGame(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        int gourdMsgType = inGourd.readInt();
                        if(gourdMsgType == Msg.FINISH_FLAG_MSG) {
                            gourdFinishFlag = true;
                            break;
                        } else if(gourdMsgType == Msg.POSITION_NOTIFY_MSG) {
                            msgController.getMsgClass(gourdMsgType, inGourd);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        int monsterMsgType = inMonster.readInt();
                        if(monsterMsgType == Msg.FINISH_FLAG_MSG) {
                            monsterFinishFlag = true;
                            break;
                        } else if(monsterMsgType == Msg.POSITION_NOTIFY_MSG) {
                            msgController.getMsgClass(monsterMsgType, inMonster);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        while(true) {
            if(gourdFinishFlag && monsterFinishFlag) {
                for(Map.Entry<Integer, GourdClass> entry : gourdFamily.entrySet()) {
                    int creatureId = entry.getKey();
                    GourdClass gourdMember = entry.getValue();
                    ImageView tempImageView = gourdMember.getCreatureImageView();
                    new PositionNotifyMsg("Gourd", creatureId,
                            tempImageView.getLayoutX(), tempImageView.getLayoutY()).sendMsg(outMonster);
                }
                for(Map.Entry<Integer, MonsterClass> entry : monsterFamily.entrySet()) {
                    int creatureId = entry.getKey();
                    MonsterClass monsterMember = entry.getValue();
                    ImageView tempImageView = monsterMember.getCreatureImageView();
                    new PositionNotifyMsg("Monster", creatureId,
                            tempImageView.getLayoutX(), tempImageView.getLayoutY()).sendMsg(outGourd);
                }
                new NoParseMsg(Msg.START_GAME_MSG).sendMsg(outGourd);
                new NoParseMsg(Msg.START_GAME_MSG).sendMsg(outMonster);
                break;
            }
        }
        startFight();
    }

    public void startFight() {
        //TODO
        while(true) {

        }
    }
}
