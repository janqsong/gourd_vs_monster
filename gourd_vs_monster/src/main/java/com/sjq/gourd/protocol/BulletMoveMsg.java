package com.sjq.gourd.protocol;

import com.sjq.gourd.log.MyLogger;

import java.io.*;

class BulletMove implements Serializable {
    public String senderName;
    public String receiverName;
    public int bulletKey;
    public double layoutX;
    public double layoutY;
    public boolean valid;
}

public class BulletMoveMsg implements Msg {
    private static final int msgType = Msg.BULLET_MOVE_MSG;
    BulletMove bulletMove = new BulletMove();

    public BulletMoveMsg() {
    }

    public BulletMoveMsg(String senderName, String receiverName, int bulletKey,
                         double layoutX, double layoutY, boolean valid) {
        bulletMove.senderName = senderName;
        bulletMove.receiverName = receiverName;
        bulletMove.bulletKey = bulletKey;
        bulletMove.layoutX = layoutX;
        bulletMove.layoutY = layoutY;
        bulletMove.valid = valid;
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

    public String getSenderName() {
        return bulletMove.senderName;
    }

    public String getReceiverName() {
        return bulletMove.receiverName;
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

    public boolean isValid() {
        return bulletMove.valid;
    }
}
