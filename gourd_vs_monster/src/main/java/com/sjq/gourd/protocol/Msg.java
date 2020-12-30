package com.sjq.gourd.protocol;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface Msg {
    // 负责传位置信息
    public static final int DISTRIBUTION_OF_CAMP_MSG = 1;
    // 负责倒计时
    public static final int COUNT_DOWN_MSG = 2;
    // 负责准备游戏阶段
    public static final int PREPARE_GAME_MSG = 3;
    // 负责通知开始游戏
    public static final int START_GAME_MSG = 4;
    // 负责通知位置变动信息
    public static final int POSITION_NOTIFY_MSG = 5;
    // 处理完成标志，每次处理完之后，都要传递处理完成
    public static final int FINISH_FLAG_MSG = 6;
    // 处理图片方向
    public static final int IMAGE_DIRECTION_MSG = 7;
    // 处理基本属性值
    public static final int ATTRIBUTE_VALUE_MSG = 8;
    // 同步客户端
    public static final int UPDATE_FLAG_MSG = 9;
    // 构造子弹
    public static final int BULLET_BUILD_MSG = 10;
    // 移动子弹
    public static final int BULLET_MOVE_MSG = 11;
    // 近战子弹协议
    public static final int BULLET_CLOSE_ATTACK_MSG = 12;
    // 删除子弹协议
    public static final int BULLET_DELETE_MSG = 13;
    // 生成装备
    public static final int EQUIPMENT_GENERATE_MSG = 14;
    // 请求拾取装备与服务器分发装备信息
    public static final int EQUIPMENT_REQUEST_MSG = 15;
    // 生物状态信息协议
    public static final int CREATURE_STATE_MSG = 16;
    // 本地回放帧同步
    public static final int FRAME_FINISH_FLAG = 17;

    public void sendMsg(ObjectOutputStream outStream);
    public void parseMsg(ObjectInputStream inStream);
}
