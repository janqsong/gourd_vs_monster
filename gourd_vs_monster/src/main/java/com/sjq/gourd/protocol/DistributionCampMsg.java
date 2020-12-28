package com.sjq.gourd.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DistributionCampMsg implements Msg {
    private static final int msgType = Msg.DISTRIBUTION_OF_CAMP_MSG;
    private String campType;

    public DistributionCampMsg() {
    }

    public DistributionCampMsg(String campType) {
        this.campType = campType;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.writeUTF(campType);
            outStream.flush();
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) {
        try {
            campType = inStream.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCampType() {
        return this.campType;
    }
}
