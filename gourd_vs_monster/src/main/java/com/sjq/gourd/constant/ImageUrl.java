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

    public static void initImageUrl() {
        gourdRightImageMap.put(CreatureId.FIRST_GOURD_ID, new Image("/images/七娃Right.png"));
        gourdRightImageMap.put(CreatureId.SECOND_GOURD_ID, new Image("/images/七娃Right.png"));
        gourdRightImageMap.put(CreatureId.THIRD_GOURD_ID, new Image("/images/七娃Right.png"));
        gourdRightImageMap.put(CreatureId.FOURTH_GOURD_ID, new Image("/images/七娃Right.png"));
        gourdRightImageMap.put(CreatureId.FIFTH_GOURD_ID, new Image("/images/七娃Right.png"));
        gourdRightImageMap.put(CreatureId.SIXTH_GOURD_ID, new Image("/images/七娃Right.png"));
        gourdRightImageMap.put(CreatureId.SEVENTH_GOURD_ID, new Image("/images/七娃Right.png"));
        gourdRightImageMap.put(CreatureId.PANGOLIN_ID, new Image("/images/七娃Right.png"));
        gourdRightImageMap.put(CreatureId.GRANDPA_ID, new Image("/images/七娃Right.png"));

        gourdLeftImageMap.put(CreatureId.FIRST_GOURD_ID, new Image("/images/七娃Left.png"));
        gourdLeftImageMap.put(CreatureId.SECOND_GOURD_ID, new Image("/images/七娃Left.png"));
        gourdLeftImageMap.put(CreatureId.THIRD_GOURD_ID, new Image("/images/七娃Left.png"));
        gourdLeftImageMap.put(CreatureId.FOURTH_GOURD_ID, new Image("/images/七娃Left.png"));
        gourdLeftImageMap.put(CreatureId.FIFTH_GOURD_ID, new Image("/images/七娃Left.png"));
        gourdLeftImageMap.put(CreatureId.SIXTH_GOURD_ID, new Image("/images/七娃Left.png"));
        gourdLeftImageMap.put(CreatureId.SEVENTH_GOURD_ID, new Image("/images/七娃Left.png"));
        gourdLeftImageMap.put(CreatureId.PANGOLIN_ID, new Image("/images/七娃Left.png"));
        gourdLeftImageMap.put(CreatureId.GRANDPA_ID, new Image("/images/七娃Left.png"));

        gourdRightSelectImageMap.put(CreatureId.FIRST_GOURD_ID, new Image("/images/七娃RightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.SECOND_GOURD_ID, new Image("/images/七娃RightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.THIRD_GOURD_ID, new Image("/images/七娃RightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.FOURTH_GOURD_ID, new Image("/images/七娃RightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.FIFTH_GOURD_ID, new Image("/images/七娃RightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.SIXTH_GOURD_ID, new Image("/images/七娃RightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.SEVENTH_GOURD_ID, new Image("/images/七娃RightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.PANGOLIN_ID, new Image("/images/七娃RightSelect.png"));
        gourdRightSelectImageMap.put(CreatureId.GRANDPA_ID, new Image("/images/七娃RightSelect.png"));

        gourdLeftSelectImageMap.put(CreatureId.FIRST_GOURD_ID, new Image("/images/七娃LeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.SECOND_GOURD_ID, new Image("/images/七娃LeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.THIRD_GOURD_ID, new Image("/images/七娃LeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.FOURTH_GOURD_ID, new Image("/images/七娃LeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.FIFTH_GOURD_ID, new Image("/images/七娃LeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.SIXTH_GOURD_ID, new Image("/images/七娃LeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.SEVENTH_GOURD_ID, new Image("/images/七娃LeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.PANGOLIN_ID, new Image("/images/七娃LeftSelect.png"));
        gourdLeftSelectImageMap.put(CreatureId.GRANDPA_ID, new Image("/images/七娃LeftSelect.png"));

        monsterRightImageMap.put(CreatureId.SNAKE_GOBLIN_ID, new Image("/images/蜈蚣精Right.png"));
        monsterRightImageMap.put(CreatureId.SCORPION_GOBLIN_ID, new Image("/images/蜈蚣精Right.png"));
        monsterRightImageMap.put(CreatureId.MONSTER1_ID, new Image("/images/蜈蚣精Right.png"));
        monsterRightImageMap.put(CreatureId.MONSTER2_ID, new Image("/images/蜈蚣精Right.png"));
        monsterRightImageMap.put(CreatureId.MONSTER3_ID, new Image("/images/蜈蚣精Right.png"));
        monsterRightImageMap.put(CreatureId.MONSTER4_ID, new Image("/images/蜈蚣精Right.png"));
        monsterRightImageMap.put(CreatureId.MONSTER5_ID, new Image("/images/蜈蚣精Right.png"));
        monsterRightImageMap.put(CreatureId.MONSTER6_ID, new Image("/images/蜈蚣精Right.png"));
        monsterRightImageMap.put(CreatureId.MONSTER7_ID, new Image("/images/蜈蚣精Right.png"));

        monsterLeftImageMap.put(CreatureId.SNAKE_GOBLIN_ID, new Image("/images/蜈蚣精Left.png"));
        monsterLeftImageMap.put(CreatureId.SCORPION_GOBLIN_ID, new Image("/images/蜈蚣精Left.png"));
        monsterLeftImageMap.put(CreatureId.MONSTER1_ID, new Image("/images/蜈蚣精Left.png"));
        monsterLeftImageMap.put(CreatureId.MONSTER2_ID, new Image("/images/蜈蚣精Left.png"));
        monsterLeftImageMap.put(CreatureId.MONSTER3_ID, new Image("/images/蜈蚣精Left.png"));
        monsterLeftImageMap.put(CreatureId.MONSTER4_ID, new Image("/images/蜈蚣精Left.png"));
        monsterLeftImageMap.put(CreatureId.MONSTER5_ID, new Image("/images/蜈蚣精Left.png"));
        monsterLeftImageMap.put(CreatureId.MONSTER6_ID, new Image("/images/蜈蚣精Left.png"));
        monsterLeftImageMap.put(CreatureId.MONSTER7_ID, new Image("/images/蜈蚣精Left.png"));

        monsterRightSelectImageMap.put(CreatureId.SNAKE_GOBLIN_ID, new Image("/images/蜈蚣精RightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.SCORPION_GOBLIN_ID, new Image("/images/蜈蚣精RightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.MONSTER1_ID, new Image("/images/蜈蚣精RightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.MONSTER2_ID, new Image("/images/蜈蚣精RightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.MONSTER3_ID, new Image("/images/蜈蚣精RightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.MONSTER4_ID, new Image("/images/蜈蚣精RightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.MONSTER5_ID, new Image("/images/蜈蚣精RightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.MONSTER6_ID, new Image("/images/蜈蚣精RightSelect.png"));
        monsterRightSelectImageMap.put(CreatureId.MONSTER7_ID, new Image("/images/蜈蚣精RightSelect.png"));

        monsterLeftSelectImageMap.put(CreatureId.SNAKE_GOBLIN_ID, new Image("/images/蜈蚣精LeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.SCORPION_GOBLIN_ID, new Image("/images/蜈蚣精LeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.MONSTER1_ID, new Image("/images/蜈蚣精LeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.MONSTER2_ID, new Image("/images/蜈蚣精LeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.MONSTER3_ID, new Image("/images/蜈蚣精LeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.MONSTER4_ID, new Image("/images/蜈蚣精LeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.MONSTER5_ID, new Image("/images/蜈蚣精LeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.MONSTER6_ID, new Image("/images/蜈蚣精LeftSelect.png"));
        monsterLeftSelectImageMap.put(CreatureId.MONSTER7_ID, new Image("/images/蜈蚣精LeftSelect.png"));
    }
}
