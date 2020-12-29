package com.sjq.gourd.protocol;

import com.sjq.gourd.log.MyLogger;

import java.io.*;

class EquipmentGenerate implements Serializable {
    public int equipmentKey;
    public int randNum;
    public double layoutX;
    public double layoutY;
}

public class EquipmentGenerateMsg implements Msg {
    private static final int msgType = Msg.EQUIPMENT_GENERATE_MSG;
    private EquipmentGenerate equipmentGenerate = new EquipmentGenerate();

    public EquipmentGenerateMsg() {
    }

    public EquipmentGenerateMsg(int equipmentKey, int randNum, double layoutX, double layoutY) {
        equipmentGenerate.equipmentKey = equipmentKey;
        equipmentGenerate.randNum = randNum;
        equipmentGenerate.layoutX = layoutX;
        equipmentGenerate.layoutY = layoutY;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.writeObject(equipmentGenerate);
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) {
        try {
            equipmentGenerate = (EquipmentGenerate) inStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getEquipmentMsg() {
        return equipmentGenerate.equipmentKey;
    }

    public int getRandNum() {
        return equipmentGenerate.randNum;
    }

    public double getLayoutX() {
        return equipmentGenerate.layoutX;
    }

    public double getLayoutY() {
        return equipmentGenerate.layoutY;
    }
}
