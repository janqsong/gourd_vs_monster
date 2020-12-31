package com.sjq.gourd.protocol;

import java.io.*;

public class FinishGameFlagMsg implements Msg {
    private final static int msgType = Msg.FINISH_GAME_FLAG_MSG;
    private String winCampType;

    public FinishGameFlagMsg() {
    }

    public FinishGameFlagMsg(String winCampType) {
        this.winCampType = winCampType;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) throws IOException {
        outStream.writeInt(msgType);
        outStream.writeObject(winCampType);
        outStream.flush();
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
        winCampType = (String) inStream.readObject();
    }

    public String getWinCampType() {
        return winCampType;
    }
}
