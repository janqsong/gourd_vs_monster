package com.sjq.gourd.protocol;

import java.io.*;

public class AttributeValueMsg implements Msg {
    private static final int msgType = Msg.ATTRIBUTE_VALUE_MSG;
    private String campType;
    private int creatureId;
    private double layoutX;
    private double layoutY;
    private int direction;
    private double currentHealth;
    private double currentMagic;
    private double currentAttack;
    private double currentDefense;
    private double currentAttackSpeed;
    private double currentMoveSpeed;

    public AttributeValueMsg() {
    }

    public AttributeValueMsg(String campType, int creatureId, double layoutX, double layoutY, int direction,
                             double currentHealth, double currentMagic, double currentAttack,
                             double currentDefense, double currentAttackSpeed, double currentMoveSpeed) {
        this.campType = campType;
        this.creatureId = creatureId;
        this.layoutX = layoutX;
        this.layoutY = layoutY;
        this.direction = direction;
        this.currentHealth = currentHealth;
        this.currentMagic = currentMagic;
        this.currentAttack = currentAttack;
        this.currentDefense = currentDefense;
        this.currentAttackSpeed = currentAttackSpeed;
        this.currentMoveSpeed = currentMoveSpeed;
    }

    @Override
    public void sendMsg(DataOutputStream outStream) {
        try {
            outStream.writeInt(msgType);
            outStream.writeUTF(campType);
            outStream.writeInt(creatureId);
            outStream.writeDouble(layoutX);
            outStream.writeDouble(layoutY);
            outStream.writeInt(direction);
            outStream.writeDouble(currentHealth);
            outStream.writeDouble(currentMagic);
            outStream.writeDouble(currentAttack);
            outStream.writeDouble(currentDefense);
            outStream.writeDouble(currentAttackSpeed);
            outStream.writeDouble(currentMoveSpeed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseMsg(DataInputStream inStream) {
        try {
            campType = inStream.readUTF();
            creatureId = inStream.readInt();
            layoutX = inStream.readDouble();
            layoutY = inStream.readDouble();
            direction = inStream.readInt();
            currentHealth = inStream.readDouble();
            currentMagic = inStream.readDouble();
            currentAttack = inStream.readDouble();
            currentDefense = inStream.readDouble();
            currentAttackSpeed = inStream.readDouble();
            currentMoveSpeed = inStream.readDouble();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCampType() {
        return campType;
    }

    public int getCreatureId() {
        return creatureId;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public double getCurrentMagic() {
        return currentMagic;
    }

    public double getCurrentAttack() {
        return currentAttack;
    }

    public double getCurrentDefense() {
        return currentDefense;
    }

    public double getCurrentAttackSpeed() {
        return currentAttackSpeed;
    }

    public double getCurrentMoveSpeed() {
        return currentMoveSpeed;
    }

    public double getLayoutX() {
        return layoutX;
    }

    public double getLayoutY() {
        return layoutY;
    }

    public int getDirection() {
        return direction;
    }

//    @Override
//    public void sendMsg(DataOutputStream outStream) {
//        try {
//            outStream.writeInt(msgType);
//            ByteArrayOutputStream buf= new ByteArrayOutputStream();
//            ObjectOutputStream outputObject = new ObjectOutputStream(buf);
//            outputObject.writeUTF(campType);
//            outputObject.writeInt(creatureId);
//            outputObject.writeDouble(layoutX);
//            outputObject.writeDouble(layoutY);
//            outputObject.writeInt(direction);
//            outputObject.writeDouble(currentHealth);
//            outputObject.writeDouble(currentMagic);
//            outputObject.writeDouble(currentAttack);
//            outputObject.writeDouble(currentDefense);
//            outputObject.writeDouble(currentAttackSpeed);
//            outputObject.writeDouble(currentMoveSpeed);
//            outputObject.close();
//            outStream.write(buf.toByteArray());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void parseMsg(DataInputStream inStream) {
//        try {
//            byte[] buf = new byte[100];
//            int num = inStream.read(buf);
//            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buf));
//            campType = in.readUTF();
//            creatureId = in.readInt();
//            layoutX = in.readDouble();
//            layoutY = in.readDouble();
//            direction = in.readInt();
//            currentHealth = in.readDouble();
//            currentMagic = in.readDouble();
//            currentAttack = in.readDouble();
//            currentDefense = in.readDouble();
//            currentAttackSpeed = in.readDouble();
//            currentMoveSpeed = in.readDouble();
//            System.out.println(campType+" "+
//            creatureId+" "+
//            layoutX+" "+
//            layoutY+" "+
//            direction+" "+
//            currentHealth+" "+
//            currentMagic+" "+
//            currentAttack+" "+
//            currentDefense+" "+
//            currentAttackSpeed+" "+
//            currentMoveSpeed);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
