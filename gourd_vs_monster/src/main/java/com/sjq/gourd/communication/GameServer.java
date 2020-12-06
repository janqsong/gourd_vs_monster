package com.sjq.gourd.communication;

import java.net.*;

import java.io.*;

class SocketRunnable implements Runnable {
    private Socket socket;
    private String threadName;
    DataInputStream in;
    DataOutputStream out;

    public SocketRunnable(Socket socket, String ip) {
        this.socket = socket;
        threadName = ip;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());   
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                System.out.println(threadName + "正在战斗");
                out.writeUTF(threadName + "正在战斗");
                Thread.sleep(10000);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}

public class GameServer extends Thread{
    private ServerSocket serverSocket;
    String player1ip;
    String player2ip;
    Thread threadplayer1;
    Thread threadplayer2;
    


    public GameServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
    @Override
    public void run() {
        int count = 1;
        while(true) {
            try {
                System.out.println("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...");
                if(count == 1) {
                    Socket socketplayer1 = serverSocket.accept();
                    player1ip = socketplayer1.getRemoteSocketAddress().toString();
                    threadplayer1 = new Thread(new SocketRunnable(socketplayer1, player1ip));
                    threadplayer1.start();
                    count++;
                } else {
                    Socket socketplayer2 = serverSocket.accept();
                    player2ip = socketplayer2.getRemoteSocketAddress().toString();
                    System.out.println(player1ip);
                    System.out.println(player1ip.compareTo(player2ip));
                    if(player1ip.compareTo(player2ip) == 0) {
                        DataOutputStream out = new DataOutputStream(socketplayer2.getOutputStream());
                        socketplayer2.close();
                        continue;
                    } else {
                        threadplayer2 = new Thread(new SocketRunnable(socketplayer2, player2ip));
                        threadplayer2.start();
                        break;
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void start_fight() {
        while(true) {

        }
    }
}
