package com.ttf.gourd.server;

import java.net.*;
import java.io.*;
import java.util.Random;

public class GameServer extends Thread{
    private ServerSocket serverSocket;
    String ipLocalHost;
    protected final Random randomNum = new Random(System.currentTimeMillis());
    private int campType = -1;

    public GameServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        ipLocalHost = InetAddress.getLocalHost().getHostAddress();
    }

    @Override
    public void run() {
        SocketController socketServerController = new SocketController(serverSocket);
        int count = 1;
        while(true) {
            try {
                System.out.println("等待远程连接，ip地址/端口号：" + ipLocalHost + "/" + serverSocket.getLocalPort() + "...");
                if(count == 1) {
                    campType = randomNum.nextInt(2);
                    Socket socket = serverSocket.accept();
                    if(campType == 0)
                        socketServerController.addGourdPlayer(socket);
                    else
                        socketServerController.addMonsterPlayer(socket);
                    count++;
                } else {
                    Socket socket = serverSocket.accept();
                    if(campType == 0)
                        socketServerController.addMonsterPlayer(socket);
                    else
                        socketServerController.addGourdPlayer(socket);
                    break;
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        try {
            socketServerController.prepareFight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
