package com.sjq.gourd.protocol;

import java.io.*;

class BulletCloseAttack implements Serializable {
    public int sourceCreatureId;
    public int targetCreatureId;
}

public class BulletCloseAttackMsg implements Msg {
    private static final int msgType = Msg.BULLET_CLOSE_ATTACK_MSG;
    private BulletCloseAttack bulletCloseAttack = new BulletCloseAttack();

    public BulletCloseAttackMsg() {
    }

    public BulletCloseAttackMsg(int sourceCreatureId, int targetCreatureId) {
        bulletCloseAttack.sourceCreatureId = sourceCreatureId;
        bulletCloseAttack.targetCreatureId = targetCreatureId;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.writeObject(bulletCloseAttack);
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) {
        try {
            bulletCloseAttack = (BulletCloseAttack) inStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSourceCreatureId() {
        return bulletCloseAttack.sourceCreatureId;
    }

    public int getTargetCreatureId() {
        return bulletCloseAttack.targetCreatureId;
    }
}
