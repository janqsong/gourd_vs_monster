package com.sjq.gourd.constant;

import java.util.HashMap;

public class ImageUrl {
    public static HashMap<String, String> gourdImageUrlMap = new HashMap<>();

    public static HashMap<String, String> monsterImageUrlMap = new HashMap<>();

    public static void initImageUrl() {
        gourdImageUrlMap.put("firstGourd", "/七娃.png");
        gourdImageUrlMap.put("secondGourd", "/七娃.png");
        gourdImageUrlMap.put("thirdGourd", "/七娃.png");
        gourdImageUrlMap.put("fourthGourd", "/七娃.png");
        gourdImageUrlMap.put("fifthGourd", "/七娃.png");
        gourdImageUrlMap.put("sixthGourd", "/七娃.png");
        gourdImageUrlMap.put("seventhGourd", "/七娃.png");
        gourdImageUrlMap.put("pangolin", "/七娃.png");
        gourdImageUrlMap.put("grandpa", "/七娃.png");

        monsterImageUrlMap.put("SnakeGoblin", "/蜈蚣精.png");
        monsterImageUrlMap.put("ScorpionGoblin", "/蜈蚣精.png");
        monsterImageUrlMap.put("monster1", "/蜈蚣精.png");
        monsterImageUrlMap.put("monster2", "/蜈蚣精.png");
        monsterImageUrlMap.put("monster3", "/蜈蚣精.png");
        monsterImageUrlMap.put("monster4", "/蜈蚣精.png");
        monsterImageUrlMap.put("monster5", "/蜈蚣精.png");
        monsterImageUrlMap.put("monster6", "/蜈蚣精.png");
        monsterImageUrlMap.put("monster7", "/蜈蚣精.png");
    }
}
