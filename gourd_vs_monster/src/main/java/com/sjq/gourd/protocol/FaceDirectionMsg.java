package com.sjq.gourd.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class FaceDirectionMsg implements Msg {
    private static final int msgType = Msg.FACE_DIRECTION_MSG;
    private String campType;
    private int creatureId;
    private int faceDirection;

    public FaceDirectionMsg() {
    }

    public FaceDirectionMsg(String campType, int creatureId, int faceDirection) {
        this.campType = campType;
        this.creatureId = creatureId;
        this.faceDirection = faceDirection;
    }

    @Override
    public void sendMsg(DataOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.writeUTF(campType);
            outStream.writeInt(creatureId);
            outStream.writeInt(faceDirection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseMsg(DataInputStream inStream) {
        try {
            campType = inStream.readUTF();
            creatureId = inStream.readInt();
            faceDirection = inStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCampType() {
        return campType;
    }

    public int getCreatureId() {
        return creatureId;
    }

    public int getFaceDirection() {
        return faceDirection;
    }
}
