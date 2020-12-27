package com.sjq.gourd.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class DistributionCampMsg implements Msg {
    private static final int msgType = Msg.DISTRIBUTION_OF_CAMP_MSG;
    private String campType;

    public DistributionCampMsg() {
    }

    public DistributionCampMsg(String campType) {
        this.campType = campType;
    }

    @Override
    public void sendMsg(DataOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.writeUTF(campType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseMsg(DataInputStream inStream) {
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
