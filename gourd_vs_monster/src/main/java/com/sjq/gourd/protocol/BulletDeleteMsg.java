package com.sjq.gourd.protocol;

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
    public void sendMsg(ObjectOutputStream outStream) throws IOException {
        outStream.writeInt(msgType);
        outStream.writeInt(bulletKey);
        outStream.flush();
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) throws IOException {
        bulletKey = inStream.readInt();
    }

    public int getBulletKey() {
        return this.bulletKey;
    }
}
