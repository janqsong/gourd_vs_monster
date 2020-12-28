package com.sjq.gourd.protocol;

import java.io.*;

public class CountDownMsg implements Msg{
    private static final int msgType = Msg.COUNT_DOWN_MSG;
    private int timeRemaining;

    public CountDownMsg() {
    }

    public CountDownMsg(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.writeInt(timeRemaining);
            outStream.flush();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) {
        try {
            timeRemaining = inStream.readInt();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int getTimeRemaining() {return timeRemaining;}
}
