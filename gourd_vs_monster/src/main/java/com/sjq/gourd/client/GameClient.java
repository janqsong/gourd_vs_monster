package com.sjq.gourd.client;

import java.net.*;
import java.io.*;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.stage.*;

public class GameClient {
    protected String ipServer;
    protected int portServer;
    protected Socket clientSocket;
    protected DataInputStream in;
    protected DataOutputStream out;

    private final MsgController msgController = new MsgController();
    private String campType;
    private final SceneController sceneController;


    public GameClient(String ipServer, int portServer, SceneController sceneController) {
        this.ipServer = ipServer;
        this.portServer = portServer;
        this.sceneController = sceneController;
    }

    public void run() {
        try {
            clientSocket = new Socket(ipServer, portServer);
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            msgController.getMsgClass(in.readInt(), in);
            campType = msgController.getCampType();
            initGameCamp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initGameCamp() {
        ImageUrl.initImageUrl();
        if(campType.equals(Constant.CampType.GOURD)) {
            System.out.println(Constant.CampType.GOURD + " startGame");
            GourdCamp gourdCamp = new GourdCamp(sceneController, in, out);
            gourdCamp.startGame();
        } else if(campType.equals(Constant.CampType.MONSTER)) {
            System.out.println(Constant.CampType.MONSTER + " startGame");
            MonsterCamp monsterCamp = new MonsterCamp(sceneController, in, out);
            monsterCamp.startGame();
        }
    }
}