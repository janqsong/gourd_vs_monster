package com.sjq.gourd.client;

import com.sjq.gourd.protocol.*;

import java.io.DataInputStream;
import java.util.ArrayDeque;
import java.util.Map;

public class MsgController {
    private String campType;

    public MsgController() {

    }

    public String getCampType() {
        return campType;
    }

    public void getMsgClass(int msgType, DataInputStream inputStream) {
        switch (msgType) {
            case Msg.NOTIFICATION_MSG: {
                new NotificationMsg().parseMsg(inputStream);
                break;
            }
            case Msg.DISTRIBUTION_OF_CAMP_MSG: {
                DistributionCampMsg distributionCampMsg = new DistributionCampMsg();
                distributionCampMsg.parseMsg(inputStream);
                campType = distributionCampMsg.getCampType();
                break;
            }
            case Msg.COUNT_DOWN_MSG: {
                new CountDownMsg().parseMsg(inputStream);
                break;
            }
            default: {
                break;
            }
        }
    }
}
