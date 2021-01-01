package com.ttf.gourd.protocol;

import java.io.*;

class AttributeValue implements Serializable {
    public String campType;
    public int creatureId;
    public double layoutX;
    public double layoutY;
    public int direction;
    public double currentHealth;
    public double currentMagic;
    public double currentAttack;
    public double currentDefense;
    public double currentAttackSpeed;
    public double currentMoveSpeed;
}

public class AttributeValueMsg implements Msg {
    private static final int msgType = Msg.ATTRIBUTE_VALUE_MSG;
    AttributeValue attributeValue = new AttributeValue();

    public AttributeValueMsg() {
    }

    public AttributeValueMsg(String campType, int creatureId, double layoutX, double layoutY, int direction,
                             double currentHealth, double currentMagic, double currentAttack,
                             double currentDefense, double currentAttackSpeed, double currentMoveSpeed) {
        attributeValue.campType = campType;
        attributeValue.creatureId = creatureId;
        attributeValue.layoutX = layoutX;
        attributeValue.layoutY = layoutY;
        attributeValue.direction = direction;
        attributeValue.currentHealth = currentHealth;
        attributeValue.currentMagic = currentMagic;
        attributeValue.currentAttack = currentAttack;
        attributeValue.currentDefense = currentDefense;
        attributeValue.currentAttackSpeed = currentAttackSpeed;
        attributeValue.currentMoveSpeed = currentMoveSpeed;
    }

    @Override
    public void sendMsg(ObjectOutputStream outStream) throws IOException {
        outStream.writeInt(msgType);
        outStream.writeObject(attributeValue);
        outStream.flush();
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
        attributeValue = (AttributeValue) inStream.readObject();
    }

    public String getCampType() {
        return attributeValue.campType;
    }

    public int getCreatureId() {
        return attributeValue.creatureId;
    }

    public double getCurrentHealth() {
        return attributeValue.currentHealth;
    }

    public double getCurrentMagic() {
        return attributeValue.currentMagic;
    }

    public double getCurrentAttack() {
        return attributeValue.currentAttack;
    }

    public double getCurrentDefense() {
        return attributeValue.currentDefense;
    }

    public double getCurrentAttackSpeed() {
        return attributeValue.currentAttackSpeed;
    }

    public double getCurrentMoveSpeed() {
        return attributeValue.currentMoveSpeed;
    }

    public double getLayoutX() {
        return attributeValue.layoutX;
    }

    public double getLayoutY() {
        return attributeValue.layoutY;
    }

    public int getDirection() {
        return attributeValue.direction;
    }
}
