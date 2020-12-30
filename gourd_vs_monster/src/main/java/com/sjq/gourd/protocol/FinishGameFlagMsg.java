package com.sjq.gourd.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FinishGameFlagMsg implements Msg{
    private final static int msgType = Msg.FINISH_GAME_FLAG_MSG;
    private String winCampType;

    public FinishGameFlagMsg() {}

    public FinishGameFlagMsg(String winCampType) {
        this.winCampType = winCampType;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.writeObject(winCampType);
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void parseMsg(ObjectInputStream inStream) {
        try {
            winCampType = (String) inStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getWinCampType() {
        return winCampType;
    }
}
