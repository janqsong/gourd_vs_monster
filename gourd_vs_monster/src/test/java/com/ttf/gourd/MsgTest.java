package com.ttf.gourd;

import com.ttf.gourd.protocol.AttributeValueMsg;
import com.ttf.gourd.protocol.*;
import org.junit.*;

import static org.junit.Assert.*;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.util.*;

public class MsgTest {

    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    File file = new File("../playbackFiles/testData");

    private final Random randomNum = new Random(System.currentTimeMillis());

    private ArrayList<Object> dataList = new ArrayList<>();

    public MsgTest() {
        try {
            file.createNewFile();
            out = new ObjectOutputStream(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateIOTestData() throws IOException {
        // 随机生成协议，存放到一个列表中
        for (int i = 0; i < 100000; i++) {
            int msgType = randomNum.nextInt(18);
            switch (msgType) {
                case Msg.DISTRIBUTION_OF_CAMP_MSG: {
                    String campType = RandomStringUtils.random(5);
                    DistributionCampMsg distributionCampMsg = new DistributionCampMsg(campType);
                    distributionCampMsg.sendMsg(out);
                    dataList.add(distributionCampMsg);
                    break;
                }
                case Msg.COUNT_DOWN_MSG: {
                    int timeRemaining = randomNum.nextInt(30);
                    CountDownMsg countDownMsg = new CountDownMsg(timeRemaining);
                    countDownMsg.sendMsg(out);
                    dataList.add(countDownMsg);
                    break;
                }
                case Msg.POSITION_NOTIFY_MSG: {
                    String campType = RandomStringUtils.random(5);
                    int creatureId = randomNum.nextInt(20);
                    double layoutX = randomNum.nextDouble() * 1000;
                    double layoutY = randomNum.nextDouble() * 1000;
                    PositionNotifyMsg positionNotifyMsg = new PositionNotifyMsg(campType, creatureId, layoutX, layoutY);
                    positionNotifyMsg.sendMsg(out);
                    dataList.add(positionNotifyMsg);
                    break;
                }
                case Msg.ATTRIBUTE_VALUE_MSG: {
                    String campType = RandomStringUtils.random(5);
                    int creatureId = randomNum.nextInt(10);
                    double layoutX = randomNum.nextDouble() * 1000;
                    double layoutY = randomNum.nextDouble() * 1000;
                    int direction = randomNum.nextInt(2);
                    double currentHealth = randomNum.nextDouble() * 10000;
                    double currentMagic = randomNum.nextDouble() * 10000;
                    double currentAttack = randomNum.nextDouble() * 10000;
                    double currentDefense = randomNum.nextDouble() * 10000;
                    double currentAttackSpeed = randomNum.nextDouble() * 10000;
                    double currentMoveSpeed = randomNum.nextDouble() * 10000;
                    AttributeValueMsg attributeValueMsg = new AttributeValueMsg(
                            campType, creatureId, layoutX, layoutY, direction, currentHealth,
                            currentMagic, currentAttack, currentDefense, currentAttackSpeed, currentMoveSpeed
                    );
                    attributeValueMsg.sendMsg(out);
                    dataList.add(attributeValueMsg);
                    break;
                }
                case Msg.BULLET_BUILD_MSG: {
                    int bulletKey = randomNum.nextInt(500);
                    String sourceCamp = RandomStringUtils.random(10);
                    int sourceCreatureId = randomNum.nextInt();
                    String targetCamp = RandomStringUtils.random(10);
                    int targetCreatureId = randomNum.nextInt();
                    int bulletType = randomNum.nextInt();
                    int bulletState = randomNum.nextInt();
                    BulletBuildMsg bulletBuildMsg = new BulletBuildMsg(bulletKey, sourceCamp, sourceCreatureId,
                            targetCamp, targetCreatureId, bulletType, bulletState);
                    bulletBuildMsg.sendMsg(out);
                    dataList.add(bulletBuildMsg);
                    break;
                }
                case Msg.BULLET_CLOSE_ATTACK_MSG: {
                    int sourceCreatureId = randomNum.nextInt();
                    int targetCreatureId = randomNum.nextInt();
                    int bulletState = randomNum.nextInt();
                    BulletCloseAttackMsg bulletCloseAttackMsg = new BulletCloseAttackMsg(
                            sourceCreatureId, targetCreatureId, bulletState
                    );
                    bulletCloseAttackMsg.sendMsg(out);
                    dataList.add(bulletCloseAttackMsg);
                    break;
                }
                case Msg.BULLET_DELETE_MSG: {
                    int bulletKey = randomNum.nextInt();
                    BulletDeleteMsg bulletDeleteMsg = new BulletDeleteMsg(bulletKey);
                    bulletDeleteMsg.sendMsg(out);
                    dataList.add(bulletDeleteMsg);
                    break;
                }
                case Msg.EQUIPMENT_GENERATE_MSG: {
                    int equipmentKey = randomNum.nextInt();
                    int randNum = randomNum.nextInt();
                    double layoutX = randomNum.nextDouble();
                    double layoutY = randomNum.nextDouble();
                    EquipmentGenerateMsg equipmentGenerateMsg = new EquipmentGenerateMsg(
                            equipmentKey, randNum, layoutX, layoutY
                    );
                    equipmentGenerateMsg.sendMsg(out);
                    dataList.add(equipmentGenerateMsg);
                    break;
                }
                case Msg.EQUIPMENT_REQUEST_MSG: {
                    String campType = RandomStringUtils.random(15);
                    int creatureId = randomNum.nextInt();
                    int equipmentKey = randomNum.nextInt();
                    EquipmentRequestMsg equipmentRequestMsg = new EquipmentRequestMsg(
                            campType, creatureId, equipmentKey
                    );
                    equipmentRequestMsg.sendMsg(out);
                    dataList.add(equipmentRequestMsg);
                    break;
                }
                case Msg.CREATURE_STATE_MSG: {
                    String campType = RandomStringUtils.random(20);
                    int creatureId = randomNum.nextInt();
                    int creatureState = randomNum.nextInt();
                    long gapTime = randomNum.nextInt();
                    CreatureStateMsg creatureStateMsg = new CreatureStateMsg(
                            campType, creatureId, creatureState, gapTime
                    );
                    creatureStateMsg.sendMsg(out);
                    dataList.add(creatureStateMsg);
                    break;
                }
                case Msg.SAME_DESTINY_MSG: {
                    String campType = RandomStringUtils.random(50);
                    int creatureId = randomNum.nextInt();
                    double deltaHealth = randomNum.nextDouble();
                    SameDestinyMsg sameDestinyMsg = new SameDestinyMsg(campType, creatureId, deltaHealth);
                    sameDestinyMsg.sendMsg(out);
                    dataList.add(sameDestinyMsg);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    @Test
    public void testIOValidity() throws IOException, ClassNotFoundException {
        // 通过字符流读入，然后与之前存在list中的值进行比较，如果出现对应的值不同，说明发送或者解析或者协议构造出现错误了。
        // 主要用于测试用于网络通信的协议是否有效，以及保存到本地是否可以正常读取。
        generateIOTestData();
        in = new ObjectInputStream(new FileInputStream(file));
        for (Object obj : dataList) {
            int msgType = in.readInt();
            switch (msgType) {
                case Msg.DISTRIBUTION_OF_CAMP_MSG: {
                    DistributionCampMsg distributionCampMsg = new DistributionCampMsg();
                    distributionCampMsg.parseMsg(in);
                    assertEquals(distributionCampMsg.getCampType(), ((DistributionCampMsg) obj).getCampType());
                    break;
                }
                case Msg.COUNT_DOWN_MSG: {
                    CountDownMsg countDownMsg = new CountDownMsg();
                    countDownMsg.parseMsg(in);
                    assertEquals(countDownMsg.getTimeRemaining(), ((CountDownMsg) obj).getTimeRemaining());
                    break;
                }
                case Msg.POSITION_NOTIFY_MSG: {
                    PositionNotifyMsg positionNotifyMsg = new PositionNotifyMsg();
                    positionNotifyMsg.parseMsg(in);
                    assertEquals(positionNotifyMsg.getCampType(), ((PositionNotifyMsg) obj).getCampType());
                    assertEquals(positionNotifyMsg.getCreatureId(), ((PositionNotifyMsg) obj).getCreatureId());
                    assertEquals(positionNotifyMsg.getLayoutX(), ((PositionNotifyMsg) obj).getLayoutX(), 0.001);
                    assertEquals(positionNotifyMsg.getLayoutY(), ((PositionNotifyMsg) obj).getLayoutY(), 0.001);
                    break;
                }
                case Msg.ATTRIBUTE_VALUE_MSG: {
                    AttributeValueMsg attributeValueMsg = new AttributeValueMsg();
                    attributeValueMsg.parseMsg(in);
                    assertEquals(attributeValueMsg.getCampType(), ((AttributeValueMsg) obj).getCampType());
                    assertEquals(attributeValueMsg.getCreatureId(), ((AttributeValueMsg) obj).getCreatureId());
                    assertEquals(attributeValueMsg.getLayoutX(), ((AttributeValueMsg) obj).getLayoutX(), 0.001);
                    assertEquals(attributeValueMsg.getLayoutY(), ((AttributeValueMsg) obj).getLayoutY(), 0.001);
                    assertEquals(attributeValueMsg.getDirection(), ((AttributeValueMsg) obj).getDirection());
                    assertEquals(attributeValueMsg.getCurrentHealth(), ((AttributeValueMsg) obj).getCurrentHealth(), 0.001);
                    assertEquals(attributeValueMsg.getCurrentMagic(), ((AttributeValueMsg) obj).getCurrentMagic(), 0.001);
                    assertEquals(attributeValueMsg.getCurrentAttack(), ((AttributeValueMsg) obj).getCurrentAttack(), 0.001);
                    assertEquals(attributeValueMsg.getCurrentDefense(), ((AttributeValueMsg) obj).getCurrentDefense(), 0.001);
                    assertEquals(attributeValueMsg.getCurrentAttackSpeed(), ((AttributeValueMsg) obj).getCurrentAttackSpeed(), 0.001);
                    assertEquals(attributeValueMsg.getCurrentMoveSpeed(), ((AttributeValueMsg) obj).getCurrentMoveSpeed(), 0.001);
                    break;
                }
                case Msg.BULLET_BUILD_MSG: {
                    BulletBuildMsg bulletBuildMsg = new BulletBuildMsg();
                    bulletBuildMsg.parseMsg(in);
                    assertEquals(bulletBuildMsg.getBulletKey(), ((BulletBuildMsg) obj).getBulletKey());
                    assertEquals(bulletBuildMsg.getSourceCamp(), ((BulletBuildMsg) obj).getSourceCamp());
                    assertEquals(bulletBuildMsg.getSourceCreatureId(), ((BulletBuildMsg) obj).getSourceCreatureId());
                    assertEquals(bulletBuildMsg.getTargetCamp(), ((BulletBuildMsg) obj).getTargetCamp());
                    assertEquals(bulletBuildMsg.getTargetCreatureId(), ((BulletBuildMsg) obj).getTargetCreatureId());
                    assertEquals(bulletBuildMsg.getBulletType(), ((BulletBuildMsg) obj).getBulletType());
                    assertEquals(bulletBuildMsg.getBulletState(), ((BulletBuildMsg) obj).getBulletState());
                    break;
                }
                case Msg.BULLET_CLOSE_ATTACK_MSG: {
                    BulletCloseAttackMsg bulletCloseAttackMsg = new BulletCloseAttackMsg();
                    bulletCloseAttackMsg.parseMsg(in);
                    assertEquals(bulletCloseAttackMsg.getSourceCreatureId(), ((BulletCloseAttackMsg) obj).getSourceCreatureId());
                    assertEquals(bulletCloseAttackMsg.getTargetCreatureId(), ((BulletCloseAttackMsg) obj).getTargetCreatureId());
                    assertEquals(bulletCloseAttackMsg.getBulletState(), ((BulletCloseAttackMsg) obj).getBulletState());
                    break;
                }
                case Msg.BULLET_DELETE_MSG: {
                    BulletDeleteMsg bulletDeleteMsg = new BulletDeleteMsg();
                    bulletDeleteMsg.parseMsg(in);
                    assertEquals(bulletDeleteMsg.getBulletKey(), ((BulletDeleteMsg) obj).getBulletKey());
                    break;
                }
                case Msg.EQUIPMENT_GENERATE_MSG: {
                    EquipmentGenerateMsg equipmentGenerateMsg = new EquipmentGenerateMsg();
                    equipmentGenerateMsg.parseMsg(in);
                    assertEquals(equipmentGenerateMsg.getEquipmentMsg(), ((EquipmentGenerateMsg) obj).getEquipmentMsg());
                    assertEquals(equipmentGenerateMsg.getRandNum(), ((EquipmentGenerateMsg) obj).getRandNum());
                    assertEquals(equipmentGenerateMsg.getLayoutX(), ((EquipmentGenerateMsg) obj).getLayoutX(), 0.001);
                    assertEquals(equipmentGenerateMsg.getLayoutY(), ((EquipmentGenerateMsg) obj).getLayoutY(), 0.001);
                    break;
                }
                case Msg.EQUIPMENT_REQUEST_MSG: {
                    EquipmentRequestMsg equipmentRequestMsg = new EquipmentRequestMsg();
                    equipmentRequestMsg.parseMsg(in);
                    assertEquals(equipmentRequestMsg.getCampType(), ((EquipmentRequestMsg) obj).getCampType());
                    assertEquals(equipmentRequestMsg.getCreatureId(), ((EquipmentRequestMsg) obj).getCreatureId());
                    assertEquals(equipmentRequestMsg.getEquipmentKey(), ((EquipmentRequestMsg) obj).getEquipmentKey());
                    break;
                }
                case Msg.CREATURE_STATE_MSG: {
                    CreatureStateMsg creatureStateMsg = new CreatureStateMsg();
                    creatureStateMsg.parseMsg(in);
                    assertEquals(creatureStateMsg.getCampType(), ((CreatureStateMsg) obj).getCampType());
                    assertEquals(creatureStateMsg.getCreatureId(), ((CreatureStateMsg) obj).getCreatureId());
                    assertEquals(creatureStateMsg.getCreatureState(), ((CreatureStateMsg) obj).getCreatureState());
                    assertEquals(creatureStateMsg.getGapTime(), ((CreatureStateMsg) obj).getGapTime());
                    break;
                }
                case Msg.SAME_DESTINY_MSG: {
                    SameDestinyMsg sameDestinyMsg = new SameDestinyMsg();
                    sameDestinyMsg.parseMsg(in);
                    assertEquals(sameDestinyMsg.getCampType(), ((SameDestinyMsg) obj).getCampType());
                    assertEquals(sameDestinyMsg.getCreatureId(), ((SameDestinyMsg) obj).getCreatureId());
                    assertEquals(sameDestinyMsg.getDeltaHealth(), ((SameDestinyMsg) obj).getDeltaHealth(), 0.001);
                    break;
                }
                default: {
                    break;
                }
            }
        }
        in.close();
        file.delete();
    }

    @Test(timeout = 50)
    public void test_sort_validity() {

    }
}