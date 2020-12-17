//package com.sjq.gourd.protocol;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//
//public class PositionNotifyMsg implements Msg{
//    private static final int msgType = Msg.POSITION_NOTIFY_MSG;
//    private String campType;
//    private int creatureId;
//    private double layoutX;
//    private double layoutY;
//
//    public PositionNotifyMsg() {
//    }
//
//    public PositionNotifyMsg(String campType, int creatureId,
//                             double layoutX, double layoutY) {
//        this.campType = campType;
//        this.creatureId = creatureId;
//        this.layoutX = layoutX;
//        this.layoutY = layoutY;
//    }
//
//    @Override
//    public void sendMsg(DataOutputStream outStream) {
//        try {
//            outStream.writeInt(msgType);
//            outStream.writeUTF(campType);
//            outStream.writeInt(creatureId);
//            outStream.writeDouble(layoutX);
//            outStream.writeDouble(layoutY);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void parseMsg(DataInputStream inStream) {
//        try {
//            campType = inStream.readUTF();
//            creatureId = inStream.readInt();
//            layoutX = inStream.readDouble();
//            layoutY = inStream.readDouble();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public String getCampType() {return campType;}
//    public int getCreatureId() {return creatureId;}
//    public double getLayoutX() {return layoutX;}
//    public double getLayoutY() {return layoutY;}
//}
