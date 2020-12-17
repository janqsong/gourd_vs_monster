//package com.sjq.gourd.protocol;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//
//public class NotificationMsg implements Msg {
//    private static final int msgType = Msg.NOTIFICATION_MSG;
//    private String notifyContent;
//
//    public NotificationMsg() {
//    }
//
//    public NotificationMsg(String notifyContent) {
//        this.notifyContent = notifyContent;
//    }
//
//    @Override
//    public void sendMsg(DataOutputStream outStream) {
//        try {
//            outStream.writeInt(msgType);
//            outStream.writeUTF(notifyContent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void parseMsg(DataInputStream inStream) {
//        try {
//            notifyContent = inStream.readUTF();
//            System.out.println("服务器：" + notifyContent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
