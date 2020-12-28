package com.sjq.gourd.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ImageDirectionMsg implements Msg{
    private static final int msgType = Msg.IMAGE_DIRECTION_MSG;
    private String campType;
    private int creatureId;
    private int direction;

    public ImageDirectionMsg() {
    }

    public ImageDirectionMsg(String campType, int creatureId, int direction) {
        this.campType = campType;
        this.creatureId = creatureId;
        this.direction = direction;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.writeUTF(campType);
            outStream.writeInt(creatureId);
            outStream.writeInt(direction);
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) {
        try {
            campType = inStream.readUTF();
            creatureId = inStream.readInt();
            direction = inStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCampType() {return campType;}
    public int getCreatureId() {return creatureId;}

    public int getDirection() {
        return direction;
    }
}
