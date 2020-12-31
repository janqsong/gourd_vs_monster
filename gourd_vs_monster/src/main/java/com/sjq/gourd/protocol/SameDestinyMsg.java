package com.sjq.gourd.protocol;

import java.io.*;

class SameDestiny implements Serializable {
    public String campType;
    public int creatureId;
    public double deltaHealth;
}

public class SameDestinyMsg implements Msg{
    private final static int msgType = Msg.SAME_DESTINY_MSG;
    private SameDestiny sameDestiny = new SameDestiny();

    public SameDestinyMsg() {}

    public SameDestinyMsg(String campType, int creatureId, double deltaHealth) {
        sameDestiny.campType = campType;
        sameDestiny.creatureId = creatureId;
        sameDestiny.deltaHealth = deltaHealth;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.writeObject(sameDestiny);
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void parseMsg(ObjectInputStream inStream) {
        try {
            sameDestiny = (SameDestiny) inStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCampType() {
        return sameDestiny.campType;
    }

    public int getCreatureId() {
        return sameDestiny.creatureId;
    }

    public double getDeltaHealth() {
        return sameDestiny.deltaHealth;
    }
}
