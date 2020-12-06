package com.sjq.gourd.communication;

import java.net.*;
import java.io.*;

public class GameClient{
    private String serverName;
    private int port;

    public GameClient(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
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
