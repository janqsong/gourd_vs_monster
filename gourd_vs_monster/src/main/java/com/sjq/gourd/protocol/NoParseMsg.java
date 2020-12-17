//package com.sjq.gourd.protocol;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//
//public class NoParseMsg implements Msg{
//    private final int msgType;
//
//    public NoParseMsg(int msgType) {
//        this.msgType = msgType;
//    }
//
//    @Override
//    public void sendMsg(DataOutputStream outStream) {
//        try {
//            outStream.writeInt(msgType);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    @Override
//    public void parseMsg(DataInputStream inStream) {
//    }
//}
