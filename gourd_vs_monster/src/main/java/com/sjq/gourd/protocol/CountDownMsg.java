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
    public void sendMsg(ObjectOutputStream outStream) throws IOException {
        outStream.writeInt(msgType);
        outStream.writeInt(timeRemaining);
        outStream.flush();
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) throws IOException {
        timeRemaining = inStream.readInt();
    }

    public int getTimeRemaining() {return timeRemaining;}
}
