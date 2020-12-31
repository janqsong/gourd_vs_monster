package com.sjq.gourd.protocol;

import java.io.*;

public class DistributionCampMsg implements Msg {
    private static final int msgType = Msg.DISTRIBUTION_OF_CAMP_MSG;
    private String campType;

    public DistributionCampMsg() {
    }

    public DistributionCampMsg(String campType) {
        this.campType = campType;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) throws IOException {
        outStream.writeInt(msgType);
        outStream.writeUTF(campType);
        outStream.flush();
        outStream.flush();
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) throws IOException {
        campType = inStream.readUTF();
    }

    public String getCampType() {
        return this.campType;
    }
}
