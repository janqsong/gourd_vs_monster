package com.sjq.gourd.protocol;

import com.sjq.gourd.creature.Creature;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ImagePosChangeMsg implements Msg {
    private static final int msgType = Msg.NOTIFICATION_MSG;
    private String creatureType;
    private String creatureId;
    private double layoutX;
    private double layoutY;

    public ImagePosChangeMsg(String creatureType, String creatureId,
                             int layoutX, int layoutY) {
        this.creatureType = creatureType;
        this.creatureId = creatureId;
        this.layoutX = layoutX;
        this.layoutY = layoutY;
    }

    @Override
    public void sendMsg(DataOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.writeUTF(creatureType);
            outStream.writeUTF(creatureId);
            outStream.writeDouble(layoutX);
            outStream.writeDouble(layoutY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseMsg(DataInputStream inStream) {
        try {
            creatureType = inStream.readUTF();
            creatureId = inStream.readUTF();
            layoutX = inStream.readDouble();
            layoutY = inStream.readDouble();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
