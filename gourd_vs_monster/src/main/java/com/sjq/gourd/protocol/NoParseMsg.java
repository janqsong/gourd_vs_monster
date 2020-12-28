package com.sjq.gourd.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NoParseMsg implements Msg{
    private final int msgType;

    public NoParseMsg(int msgType) {
        this.msgType = msgType;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void parseMsg(ObjectInputStream inStream) {
    }
}
