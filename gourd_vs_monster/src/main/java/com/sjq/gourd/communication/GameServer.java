package com.sjq.gourd.communication;

import java.net.*;
import java.io.*;


public class GameServer extends Thread{
    private ServerSocket serverSocket;

    public GameServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        //设置等待时间，可以在可视化界面上显示出这个倒计时
        serverSocket.setSoTimeout(10000);
    }
    @Override
    public void run() {
        while(true) {
            try {
                System.out.println("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println("远程主机地址：" + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                System.out.println(in.readUTF());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("谢谢连接我：" + server.getLocalSocketAddress() + "\nGoodbye!");
                server.close();
            } catch(SocketTimeoutException s) {
               System.out.println("Socket timed out!");
               break;
            } catch(IOException e) {
               e.printStackTrace();
               break;
            }
        }
    }
}
