package com.sjq.gourd.server;

import java.net.*;
import java.io.*;

public class GameServer extends Thread{
    private ServerSocket serverSocket;
    String ipLocalHost;
    
    public GameServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        ipLocalHost = InetAddress.getLocalHost().getHostAddress();
    }

    @Override
    public void run() {
        SocketController socketServerController = new SocketController();
        int count = 1;
        while(true) {
            try {
                System.out.println("等待远程连接，ip地址/端口号：" + ipLocalHost + "/" + serverSocket.getLocalPort() + "...");
                if(count == 1) {
                    Socket socketGourd = serverSocket.accept();
                    socketServerController.addGourdPlayer(socketGourd);
                    count++;
                } else {
                    Socket socketMonster = serverSocket.accept();
                    socketServerController.addMonsterPlayer(socketMonster);
                    break;
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        socketServerController.run();
    }
}
