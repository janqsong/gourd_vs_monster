package com.sjq.gourd.communication;

import java.net.*;
import java.io.*;

public class GameClient extends Thread{
    private String serverName;
    private int port;
    private Socket client;
    private DataInputStream in;

    public GameClient(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            client = new Socket(serverName, port);
            OutputStream outToServer = client.getOutputStream();
            InputStream inFromServer = client.getInputStream();
            in = new DataInputStream(inFromServer);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        while(true) {
            try {
                System.out.println("client" + in.readUTF());
                if(in.readUTF().equals("close")) {
                    client.close();
                    break;
                }
            } catch(IOException e) {
               e.printStackTrace();
               break;
            }
        }
    }

    public void start_connect_server() {
        try {
            Socket client = new Socket(serverName, port);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
    
            out.writeUTF("Hello from " + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            System.out.println("服务器响应： " + in.readUTF());
            client.close();
        } catch(IOException e) {
           e.printStackTrace();
        }
    }


}
