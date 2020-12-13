package com.sjq.gourd.constant;

import java.util.HashMap;

public class ImageUrl {
    public static HashMap<Integer, String> gourdImageUrlMap = new HashMap<>();
    public static HashMap<Integer, String> gourdSelectImageUrlMap = new HashMap<>();

    public static HashMap<Integer, String> monsterImageUrlMap = new HashMap<>();
    public static HashMap<Integer, String> monsterSelectImageUrlMap = new HashMap<>();

    public static void initImageUrl() {
        gourdImageUrlMap.put(CreatureId.FIRST_GOURD_ID, "/七娃.png");
        gourdImageUrlMap.put(CreatureId.SECOND_GOURD_ID, "/七娃.png");
        gourdImageUrlMap.put(CreatureId.THIRD_GOURD_ID, "/七娃.png");
        gourdImageUrlMap.put(CreatureId.FOURTH_GOURD_ID, "/七娃.png");
        gourdImageUrlMap.put(CreatureId.FIFTH_GOURD_ID, "/七娃.png");
        gourdImageUrlMap.put(CreatureId.SIXTH_GOURD_ID, "/七娃.png");
        gourdImageUrlMap.put(CreatureId.SEVENTH_GOURD_ID, "/七娃.png");
        gourdImageUrlMap.put(CreatureId.PANGOLIN_ID, "/七娃.png");
        gourdImageUrlMap.put(CreatureId.GRANDPA_ID, "/七娃.png");
        gourdSelectImageUrlMap.put(CreatureId.FIRST_GOURD_ID, "/七娃Select.png");
        gourdSelectImageUrlMap.put(CreatureId.SECOND_GOURD_ID, "/七娃Select.png");
        gourdSelectImageUrlMap.put(CreatureId.THIRD_GOURD_ID, "/七娃Select.png");
        gourdSelectImageUrlMap.put(CreatureId.FOURTH_GOURD_ID, "/七娃Select.png");
        gourdSelectImageUrlMap.put(CreatureId.FIFTH_GOURD_ID, "/七娃Select.png");
        gourdSelectImageUrlMap.put(CreatureId.SIXTH_GOURD_ID, "/七娃Select.png");
        gourdSelectImageUrlMap.put(CreatureId.SEVENTH_GOURD_ID, "/七娃Select.png");
        gourdSelectImageUrlMap.put(CreatureId.PANGOLIN_ID, "/七娃Select.png");
        gourdSelectImageUrlMap.put(CreatureId.GRANDPA_ID, "/七娃Select.png");

        monsterImageUrlMap.put(CreatureId.SNAKE_GOBLIN_ID, "/蜈蚣精.png");
        monsterImageUrlMap.put(CreatureId.SCORPION_GOBLIN_ID, "/蜈蚣精.png");
        monsterImageUrlMap.put(CreatureId.MONSTER1_ID, "/蜈蚣精.png");
        monsterImageUrlMap.put(CreatureId.MONSTER2_ID, "/蜈蚣精.png");
        monsterImageUrlMap.put(CreatureId.MONSTER3_ID, "/蜈蚣精.png");
        monsterImageUrlMap.put(CreatureId.MONSTER4_ID, "/蜈蚣精.png");
        monsterImageUrlMap.put(CreatureId.MONSTER5_ID, "/蜈蚣精.png");
        monsterImageUrlMap.put(CreatureId.MONSTER6_ID, "/蜈蚣精.png");
        monsterImageUrlMap.put(CreatureId.MONSTER7_ID, "/蜈蚣精.png");
        monsterSelectImageUrlMap.put(CreatureId.SNAKE_GOBLIN_ID, "/蜈蚣精Select.png");
        monsterSelectImageUrlMap.put(CreatureId.SCORPION_GOBLIN_ID, "/蜈蚣精Select.png");
        monsterSelectImageUrlMap.put(CreatureId.MONSTER1_ID, "/蜈蚣精Select.png");
        monsterSelectImageUrlMap.put(CreatureId.MONSTER2_ID, "/蜈蚣精Select.png");
        monsterSelectImageUrlMap.put(CreatureId.MONSTER3_ID, "/蜈蚣精Select.png");
        monsterSelectImageUrlMap.put(CreatureId.MONSTER4_ID, "/蜈蚣精Select.png");
        monsterSelectImageUrlMap.put(CreatureId.MONSTER5_ID, "/蜈蚣精Select.png");
        monsterSelectImageUrlMap.put(CreatureId.MONSTER6_ID, "/蜈蚣精Select.png");
        monsterSelectImageUrlMap.put(CreatureId.MONSTER7_ID, "/蜈蚣精Select.png");
    }
}
