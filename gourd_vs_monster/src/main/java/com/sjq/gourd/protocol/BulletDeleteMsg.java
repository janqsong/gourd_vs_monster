package com.sjq.gourd.protocol;

import com.sjq.gourd.log.MyLogger;

import java.io.*;

public class BulletDeleteMsg implements Msg {
    private static final int msgType = Msg.BULLET_DELETE_MSG;
    private int bulletKey;

    public BulletDeleteMsg() {
    }

    public BulletDeleteMsg(int bulletKey) {
        this.bulletKey = bulletKey;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.writeInt(bulletKey);
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) {
        try {
            bulletKey = inStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getBulletKey() {
        return this.bulletKey;
    }
}
