package com.sjq.gourd.protocol;

import com.sjq.gourd.log.MyLogger;

import java.io.*;

class BulletMove implements Serializable {
    public int bulletKey;
    public double layoutX;
    public double layoutY;
}

public class BulletMoveMsg implements Msg {
    private static final int msgType = Msg.BULLET_MOVE_MSG;
    BulletMove bulletMove = new BulletMove();

    public BulletMoveMsg() {
    }

    public BulletMoveMsg(int bulletKey, double layoutX, double layoutY) {
        bulletMove.bulletKey = bulletKey;
        bulletMove.layoutX = layoutX;
        bulletMove.layoutY = layoutY;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.writeObject(bulletMove);
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) {
        try {
            bulletMove = (BulletMove) inStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getBulletKey() {
        return bulletMove.bulletKey;
    }

    public double getLayoutX() {
        return bulletMove.layoutX;
    }

    public double getLayoutY() {
        return bulletMove.layoutY;
    }
}
