package com.ttf.gourd.protocol;

import java.io.*;

public class NoParseMsg implements Msg {
    private final int msgType;

    public NoParseMsg(int msgType) {
        this.msgType = msgType;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) throws IOException {
        outStream.writeInt(msgType);
        outStream.flush();
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) {
    }
}
