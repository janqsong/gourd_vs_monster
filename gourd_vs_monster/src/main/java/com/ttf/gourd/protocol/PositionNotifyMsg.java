package com.ttf.gourd.protocol;

import java.io.*;

class PositionNotify implements Serializable {
    public String campType;
    public int creatureId;
    public double layoutX;
    public double layoutY;
}

public class PositionNotifyMsg implements Msg {
    private static final int msgType = POSITION_NOTIFY_MSG;
    PositionNotify positionNotify = new PositionNotify();

    public PositionNotifyMsg() {
    }

    public PositionNotifyMsg(String campType, int creatureId,
                             double layoutX, double layoutY) {
        positionNotify.campType = campType;
        positionNotify.creatureId = creatureId;
        positionNotify.layoutX = layoutX;
        positionNotify.layoutY = layoutY;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) throws IOException {
        outStream.writeInt(msgType);
        outStream.writeObject(positionNotify);
        outStream.flush();
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
        positionNotify = (PositionNotify) inStream.readObject();
    }

    public String getCampType() {
        return positionNotify.campType;
    }

    public int getCreatureId() {
        return positionNotify.creatureId;
    }

    public double getLayoutX() {
        return positionNotify.layoutX;
    }

    public double getLayoutY() {
        return positionNotify.layoutY;
    }
}
