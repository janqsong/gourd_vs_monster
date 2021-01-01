package com.ttf.gourd.protocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface Msg {
    // 负责传位置信息
    public static final int DISTRIBUTION_OF_CAMP_MSG = 0;
    // 负责倒计时

    public static final int COUNT_DOWN_MSG = 1;
    // 负责准备游戏阶段
    public static final int PREPARE_GAME_MSG = 2;
    // 负责通知开始游戏
    public static final int START_GAME_MSG = 3;
    // 负责通知位置变动信息
    public static final int POSITION_NOTIFY_MSG = 4;
    // 处理完成标志，每次处理完之后，都要传递处理完成
    public static final int FINISH_FLAG_MSG = 5;
    // 处理基本属性值
    public static final int ATTRIBUTE_VALUE_MSG = 6;
    // 构造子弹
    public static final int BULLET_BUILD_MSG = 7;
    // 移动子弹
    public static final int BULLET_MOVE_MSG = 8;
    // 近战子弹协议
    public static final int BULLET_CLOSE_ATTACK_MSG = 9;
    // 删除子弹协议
    public static final int BULLET_DELETE_MSG = 10;
    // 生成装备
    public static final int EQUIPMENT_GENERATE_MSG = 11;
    // 请求拾取装备与服务器分发装备信息
    public static final int EQUIPMENT_REQUEST_MSG = 12;
    // 生物状态信息协议
    public static final int CREATURE_STATE_MSG = 13;
    // 本地回放帧同步
    public static final int FRAME_FINISH_FLAG_MSG = 14;
    // 结束游戏
    public static final int FINISH_GAME_FLAG_MSG = 15;
    // 蝎子精扣血操作
    public static final int SAME_DESTINY_MSG = 16;
    // 接收器退出工作
    public static final int SOCKET_DISCONNECT_MSG = 17;

    public void sendMsg(ObjectOutputStream outStream) throws IOException;
    public void parseMsg(ObjectInputStream inStream) throws IOException, ClassNotFoundException;
}
