package com.sjq.gourd.protocol;

import java.io.*;

class EquipmentRequest implements Serializable {
    public String campType;
    public int creatureId;
    public int equipmentKey;
}

public class EquipmentRequestMsg implements Msg {
    private static final int msgType = Msg.EQUIPMENT_REQUEST_MSG;
    EquipmentRequest equipmentRequest = new EquipmentRequest();

    public EquipmentRequestMsg() {
    }

    public EquipmentRequestMsg(String campType, int creatureId, int equipmentKey) {
        equipmentRequest.campType = campType;
        equipmentRequest.creatureId = creatureId;
        equipmentRequest.equipmentKey = equipmentKey;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) throws IOException {
        outStream.writeInt(msgType);
        outStream.writeObject(equipmentRequest);
        outStream.flush();
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
        equipmentRequest = (EquipmentRequest) inStream.readObject();
    }

    public String getCampType() {
        return equipmentRequest.campType;
    }

    public int getCreatureId() {
        return equipmentRequest.creatureId;
    }

    public int getEquipmentKey() {
        return equipmentRequest.equipmentKey;
    }
}
