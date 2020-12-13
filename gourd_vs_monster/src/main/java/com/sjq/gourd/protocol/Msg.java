package com.sjq.gourd.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface Msg {
    public static final int NOTIFICATION_MSG = 1;
    public static final int DISTRIBUTION_OF_CAMP_MSG = 2;
    public static final int COUNT_DOWN_MSG = 3;
    public static final int PREPARE_GAME_MSG = 4;
    public static final int START_GAME_MSG = 5;
    public static final int POSITION_NOTIFY_MSG = 6;
    public static final int FINISH_FLAG_MSG = 7;

    public void sendMsg(DataOutputStream outStream);
    public void parseMsg(DataInputStream inStream);
}
