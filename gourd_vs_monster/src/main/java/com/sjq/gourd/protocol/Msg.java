package com.sjq.gourd.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface Msg {
    public static final int NOTIFICATION_MSG = 1;
    public static final int DISTRIBUTION_OF_CAMP_MSG = 2;
    public static final int COUNT_DOWN_MSG = 3;

    public void sendMsg(DataOutputStream outStream);
    public void parseMsg(DataInputStream inStream);
}
