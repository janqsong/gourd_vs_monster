//package com.sjq.gourd.protocol;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//
//public class BulletCloseAttackMsg implements Msg {
//    private static final int msgType = Msg.BULLET_CLOSE_ATTACK_MSG;
//    private int bulletKey;
//    private double layoutX;
//    private double layoutY;
//    private boolean valid;
//
//    public BulletCloseAttackMsg() {
//    }
//
//    public BulletCloseAttackMsg(String senderName, String receiverName, int bulletKey,
//                         double layoutX, double layoutY, boolean valid) {
//        this.bulletKey = bulletKey;
//        this.layoutX = layoutX;
//        this.layoutY = layoutY;
//        this.valid = valid;
//    }
//
//    @Override
//    public void sendMsg(DataOutputStream outStream) {
//        try {
//            outStream.writeInt(msgType);
//            outStream.writeUTF(senderName);
//            outStream.writeUTF(receiverName);
//            outStream.writeInt(bulletKey);
//            outStream.writeDouble(layoutX);
//            outStream.writeDouble(layoutY);
//            outStream.writeBoolean(valid);
////            MyLogger.log.info("senderName: " + senderName + " " +
////                    "receiverName: " + receiverName + " " +
////                    "bulletKey: " + bulletKey + " " +
////                    "layoutX: " + layoutX + " " +
////                    "layoutY: " + layoutY + " " +
////                    "valid: " + valid);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void parseMsg(DataInputStream inStream) {
//        try {
//            senderName = inStream.readUTF();
//            receiverName = inStream.readUTF();
//            bulletKey = inStream.readInt();
//            layoutX = inStream.readDouble();
//            layoutY = inStream.readDouble();
//            valid = inStream.readBoolean();
////            MyLogger.log.info("senderName: " + senderName + " " +
////                    "receiverName: " + receiverName + " " +
////                    "bulletKey: " + bulletKey + " " +
////                    "layoutX: " + layoutX + " " +
////                    "layoutY: " + layoutY + " " +
////                    "valid: " + valid);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public String getSenderName() {
//        return senderName;
//    }
//
//    public String getReceiverName() {
//        return receiverName;
//    }
//
//    public int getBulletKey() {
//        return bulletKey;
//    }
//
//    public double getLayoutX() {
//        return layoutX;
//    }
//
//    public double getLayoutY() {
//        return layoutY;
//    }
//
//    public boolean isValid() {
//        return valid;
//    }
//}
