package com.sjq.gourd.constant;

import javafx.scene.image.Image;

import java.util.HashMap;

public class ImageUrl {
    public static HashMap<Integer, Image> gourdRightImageMap = new HashMap<>();
    public static HashMap<Integer, Image> gourdLeftImageMap = new HashMap<>();
    public static HashMap<Integer, Image> gourdRightSelectImageMap = new HashMap<>();
    public static HashMap<Integer, Image> gourdLeftSelectImageMap = new HashMap<>();

    public static HashMap<Integer, Image> monsterRightImageMap = new HashMap<>();
    public static HashMap<Integer, Image> monsterLeftImageMap = new HashMap<>();
    public static HashMap<Integer, Image> monsterRightSelectImageMap = new HashMap<>();
    public static HashMap<Integer, Image> monsterLeftSelectImageMap = new HashMap<>();

    public static HashMap<Integer, Image> closeAttackImageMap = new HashMap<>();

    public static void initImageUrl() {
        gourdRightImageMap.put(CreatureId.FIRST_GOURD_ID, new Image("/images/gourdImages/firstGourdRight.png"));
        gourdRightImageMap.put(CreatureId.SECOND_GOURD_ID, new Image("/images/gourdImages/secondGourdRight.png"));
        gourdRightImageMap.put(CreatureId.THIRD_GOURD_ID, new Image("/images/gourdImages/thirdGourdRight.png"));
        gourdRightImageMap.put(CreatureId.FOURTH_GOURD_ID, new Image("/images/gourdImages/fourthGourdRight.png"));
        gourdRightImageMap.put(CreatureId.FIFTH_GOURD_ID, new Image("/images/gourdImages/fifthGourdRight.png"));
        gourdRightImageMap.put(CreatureId.SIXTH_GOURD_ID, new Image("/images/gourdImages/sixthGourdRight.png"));
        gourdRightImageMap.put(CreatureId.SEVENTH_GOURD_ID, new Image("/images/gourdImages/seventhGourdRight.png"));
        gourdRightImageMap.put(CreatureId.PANGOLIN_ID, new Image("/images/gourdImages/pangolinRight.png"));
        gourdRightImageMap.put(CreatureId.GRANDPA_ID, new Image("/images/gourdImages/grandpaRight.png"));

        gourdLeftImageMap.put(CreatureId.FIRST_GOURD_ID, new Image("/images/gourdImages/firstGourdLeft.png"));
        gourdLeftImageMap.put(CreatureId.SECOND_GOURD_ID, new Image("/images/gourdImages/secondGourdLeft.png"));
        gourdLeftImageMap.put(CreatureId.THIRD_GOURD_ID, new Image("/images/gourdImages/thirdGourdLeft.png"));
        gourdLeftImageMap.put(CreatureId.FOURTH_GOURD_ID, new Image("/images/gourdImages/fourthGourdLeft.png"));
        gourdLeftImageMap.put(CreatureId.FIFTH_GOURD_ID, new Image("/images/gourdImages/fifthGourdLeft.png"));
        gourdLeftImageMap.put(CreatureId.SIXTH_GOURD_ID, new Image("/images/gourdImages/sixthGourdLeft.png"));
        gourdLeftImageMap.put(CreatureId.SEVENTH_GOURD_ID, new Image("/images/gourdImages/seventhGourdLeft.png"));
        gourdLeftImageMap.put(CreatureId.PANGOLIN_ID, new Image("/images/gourdImages/pangolinLeft.png"));
        gourdLeftImageMap.put(CreatureId.GRANDPA_ID, new Image("/images/gourdImages/grandpaLeft.png"));

        gourdRightSelectImageMap.put(CreatureId.FIRST_GOURD_ID, new Image("/images/gourdImages/firstGourdRightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.SECOND_GOURD_ID, new Image("/images/gourdImages/secondGourdRightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.THIRD_GOURD_ID, new Image("/images/gourdImages/thirdGourdRightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.FOURTH_GOURD_ID, new Image("/images/gourdImages/fourthGourdRightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.FIFTH_GOURD_ID, new Image("/images/gourdImages/fifthGourdRightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.SIXTH_GOURD_ID, new Image("/images/gourdImages/sixthGourdRightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.SEVENTH_GOURD_ID, new Image("/images/gourdImages/seventhGourdRightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.PANGOLIN_ID, new Image("/images/gourdImages/pangolinRightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.GRANDPA_ID, new Image("/images/gourdImages/grandpaRightSelect.png"));

        gourdLeftSelectImageMap.put(CreatureId.FIRST_GOURD_ID, new Image("/images/gourdImages/firstGourdLeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.SECOND_GOURD_ID, new Image("/images/gourdImages/secondGourdLeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.THIRD_GOURD_ID, new Image("/images/gourdImages/thirdGourdLeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.FOURTH_GOURD_ID, new Image("/images/gourdImages/fourthGourdLeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.FIFTH_GOURD_ID, new Image("/images/gourdImages/fifthGourdLeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.SIXTH_GOURD_ID, new Image("/images/gourdImages/sixthGourdLeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.SEVENTH_GOURD_ID, new Image("/images/gourdImages/seventhGourdLeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.PANGOLIN_ID, new Image("/images/gourdImages/pangolinLeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.GRANDPA_ID, new Image("/images/gourdImages/grandpaLeftSelect.png"));

        monsterRightImageMap.put(CreatureId.SNAKE_MONSTER_ID, new Image("/images/monsterImages/snakeMonsterRight.png"));
        monsterRightImageMap.put(CreatureId.SCORPION_MONSTER_ID, new Image("/images/monsterImages/scorpionMonsterRight.png"));
        monsterRightImageMap.put(CreatureId.MONSTER1_ID, new Image("/images/monsterImages/centipedeMonsterRight.png"));
        monsterRightImageMap.put(CreatureId.MONSTER2_ID, new Image("/images/monsterImages/batMonsterRight.png"));
        monsterRightImageMap.put(CreatureId.MONSTER3_ID, new Image("/images/monsterImages/crocodileMonsterRight.png"));
        monsterRightImageMap.put(CreatureId.MONSTER4_ID, new Image("/images/monsterImages/toadMonsterRight.png"));
        monsterRightImageMap.put(CreatureId.MONSTER5_ID, new Image("/images/monsterImages/centipedeMonsterRight.png"));
        monsterRightImageMap.put(CreatureId.MONSTER6_ID, new Image("/images/monsterImages/centipedeMonsterRight.png"));
        monsterRightImageMap.put(CreatureId.MONSTER7_ID, new Image("/images/monsterImages/centipedeMonsterRight.png"));

        monsterLeftImageMap.put(CreatureId.SNAKE_MONSTER_ID, new Image("/images/monsterImages/snakeMonsterLeft.png"));
        monsterLeftImageMap.put(CreatureId.SCORPION_MONSTER_ID, new Image("/images/monsterImages/scorpionMonsterLeft.png"));
        monsterLeftImageMap.put(CreatureId.MONSTER1_ID, new Image("/images/monsterImages/centipedeMonsterLeft.png"));
        monsterLeftImageMap.put(CreatureId.MONSTER2_ID, new Image("/images/monsterImages/batMonsterLeft.png"));
        monsterLeftImageMap.put(CreatureId.MONSTER3_ID, new Image("/images/monsterImages/crocodileMonsterLeft.png"));
        monsterLeftImageMap.put(CreatureId.MONSTER4_ID, new Image("/images/monsterImages/toadMonsterLeft.png"));
        monsterLeftImageMap.put(CreatureId.MONSTER5_ID, new Image("/images/monsterImages/centipedeMonsterLeft.png"));
        monsterLeftImageMap.put(CreatureId.MONSTER6_ID, new Image("/images/monsterImages/centipedeMonsterLeft.png"));
        monsterLeftImageMap.put(CreatureId.MONSTER7_ID, new Image("/images/monsterImages/centipedeMonsterLeft.png"));

        monsterRightSelectImageMap.put(CreatureId.SNAKE_MONSTER_ID, new Image("/images/monsterImages/snakeMonsterRightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.SCORPION_MONSTER_ID, new Image("/images/monsterImages/scorpionMonsterRightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.MONSTER1_ID, new Image("/images/monsterImages/centipedeMonsterRightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.MONSTER2_ID, new Image("/images/monsterImages/batMonsterRightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.MONSTER3_ID, new Image("/images/monsterImages/crocodileMonsterRightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.MONSTER4_ID, new Image("/images/monsterImages/toadMonsterRightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.MONSTER5_ID, new Image("/images/monsterImages/centipedeMonsterRightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.MONSTER6_ID, new Image("/images/monsterImages/centipedeMonsterRightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.MONSTER7_ID, new Image("/images/monsterImages/centipedeMonsterRightSelect.png"));

        monsterLeftSelectImageMap.put(CreatureId.SNAKE_MONSTER_ID, new Image("/images/monsterImages/snakeMonsterLeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.SCORPION_MONSTER_ID, new Image("/images/monsterImages/scorpionMonsterLeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.MONSTER1_ID, new Image("/images/monsterImages/centipedeMonsterLeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.MONSTER2_ID, new Image("/images/monsterImages/batMonsterLeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.MONSTER3_ID, new Image("/images/monsterImages/crocodileMonsterLeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.MONSTER4_ID, new Image("/images/monsterImages/toadMonsterLeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.MONSTER5_ID, new Image("/images/monsterImages/centipedeMonsterLeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.MONSTER6_ID, new Image("/images/monsterImages/centipedeMonsterLeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.MONSTER7_ID, new Image("/images/monsterImages/centipedeMonsterLeftSelect.png"));

        closeAttackImageMap.put(Constant.ClawType.FIRST_CLAW,new Image("/images/attackImages/firstClaw.png"));
        closeAttackImageMap.put(Constant.ClawType.SECOND_CLAW,new Image("/images/attackImages/secondClaw.png"));
        closeAttackImageMap.put(Constant.ClawType.THIRD_CLAW,new Image("/images/attackImages/thirdClaw.png"));
        closeAttackImageMap.put(Constant.ClawType.FOURTH_CLAW,new Image("/images/attackImages/fourthClaw.png"));
        //closeAttackImageMap.put(Constant.ClawType.NONE_CLAW,new Image("/images/attackImages/noneClaw.png"));
    }
}
