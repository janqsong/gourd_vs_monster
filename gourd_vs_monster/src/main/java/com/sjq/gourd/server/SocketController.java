package com.sjq.gourd.server;

import java.net.*;
import java.io.*;

import com.sjq.gourd.protocol.*;
import javafx.application.Application;

public class SocketController {
    private Socket socketPlayerGourd;
    private Socket socketPlayerMonster;
    private DataInputStream inGourd;
    private DataOutputStream outGourd;
    private DataInputStream inMonster;
    private DataOutputStream outMonster;

    public SocketController() {
    }

    public void addGourdPlayer(Socket socket) {
        socketPlayerGourd = socket;
        try {
            inGourd = new DataInputStream(socketPlayerGourd.getInputStream());
            outGourd = new DataOutputStream(socketPlayerGourd.getOutputStream());
            new NotificationMsg("恭喜您，加载游戏成功，您的阵营是“葫芦娃”，请等待其他玩家加入！").sendMsg(outGourd);
            new DistributionCampMsg("Gourd").sendMsg(outGourd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMonsterPlayer(Socket socket) {
        socketPlayerMonster = socket;
        try {
            inMonster = new DataInputStream(socketPlayerMonster.getInputStream());
            outMonster = new DataOutputStream(socketPlayerMonster.getOutputStream());
            new NotificationMsg("恭喜您，加载游戏成功，您的阵营是“妖精”").sendMsg(outMonster);
            new DistributionCampMsg("Monster").sendMsg(outMonster);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void run() {
        new NotificationMsg("匹配成功，准备战斗！").sendMsg(outGourd);
        new NotificationMsg("匹配成功，准备战斗！").sendMsg(outMonster);
//        for(int i = 0; i < 30; i++) {
//            new CountDownMsg(30 - i).sendMsg(outGourd);
//            new CountDownMsg(30 - i).sendMsg(outMonster);
//            try {
//                Thread.sleep(1000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        new ServerScene().startGame();
//        while(true) {
//            System.out.println("正在战斗");
//            try {
//                outGourd.writeUTF("葫芦娃正在战斗");
//                outMonster.writeUTF("妖精正在战斗");
//                Thread.sleep(10000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

}
