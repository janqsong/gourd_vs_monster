package com.sjq.gourd.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class BulletBuildMsg implements Msg {
    private static final int msgType = Msg.BULLET_BUILD_MSG;
    private String senderName;
    private String receiverName;
    private int bulletKey;
    private String sourceCamp;
    private int sourceCreatureId;
    private String targetCamp;
    private int targetCreatureId;
    private int bulletType;
    private int bulletState;

    public BulletBuildMsg() {
    }

    public BulletBuildMsg(String senderName, String receiverName, int bulletKey, String sourceCamp, int sourceCreatureId,
                          String targetCamp, int targetCreatureId, int bulletType, int bulletState) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.bulletKey = bulletKey;
        this.sourceCamp = sourceCamp;
        this.sourceCreatureId = sourceCreatureId;
        this.targetCamp = targetCamp;
        this.targetCreatureId = targetCreatureId;
        this.bulletType = bulletType;
        this.bulletState = bulletState;
    }

    @Override
    public void sendMsg(DataOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.writeUTF(senderName);
            outStream.writeUTF(receiverName);
            outStream.writeInt(bulletKey);
            outStream.writeUTF(sourceCamp);
            outStream.writeInt(sourceCreatureId);
            outStream.writeUTF(targetCamp);
            outStream.writeInt(targetCreatureId);
            outStream.writeInt(bulletType);
            outStream.writeInt(bulletState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseMsg(DataInputStream inStream) {
        try {
            senderName = inStream.readUTF();
            receiverName = inStream.readUTF();
            bulletKey = inStream.readInt();
            sourceCamp = inStream.readUTF();
            sourceCreatureId = inStream.readInt();
            targetCamp = inStream.readUTF();
            targetCreatureId = inStream.readInt();
            bulletType = inStream.readInt();
            bulletState = inStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public int getBulletKey() {
        return bulletKey;
    }

    public String getSourceCamp() {
        return sourceCamp;
    }

    public int getSourceCreatureId() {
        return sourceCreatureId;
    }

    public String getTargetCamp() {
        return targetCamp;
    }

    public int getTargetCreatureId() {
        return targetCreatureId;
    }

    public int getBulletType() {
        return bulletType;
    }

    public int getBulletState() {
        return bulletState;
    }
}
