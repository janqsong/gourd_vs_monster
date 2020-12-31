package com.sjq.gourd.protocol;

import java.io.*;

class BulletCloseAttack implements Serializable {
    public int sourceCreatureId;
    public int targetCreatureId;
    public int bulletState;
}

public class BulletCloseAttackMsg implements Msg {
    private static final int msgType = Msg.BULLET_CLOSE_ATTACK_MSG;
    private BulletCloseAttack bulletCloseAttack = new BulletCloseAttack();

    public BulletCloseAttackMsg() {
    }

    public BulletCloseAttackMsg(int sourceCreatureId, int targetCreatureId, int bulletState) {
        bulletCloseAttack.sourceCreatureId = sourceCreatureId;
        bulletCloseAttack.targetCreatureId = targetCreatureId;
        bulletCloseAttack.bulletState = bulletState;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) throws IOException {
        outStream.writeInt(msgType);
        outStream.writeObject(bulletCloseAttack);
        outStream.flush();
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
        bulletCloseAttack = (BulletCloseAttack) inStream.readObject();
    }

    public int getSourceCreatureId() {
        return bulletCloseAttack.sourceCreatureId;
    }

    public int getTargetCreatureId() {
        return bulletCloseAttack.targetCreatureId;
    }

    public int getBulletState() {
        return bulletCloseAttack.bulletState;
    }
}
