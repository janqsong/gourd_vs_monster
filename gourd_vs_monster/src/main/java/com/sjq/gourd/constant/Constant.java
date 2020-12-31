package com.sjq.gourd.constant;

public class Constant {
    public static final int STAGE_WIDTH = 1360;
    public static final int STAGE_HEIGHT = 740;

    public static final double FIGHT_PANE_WIDTH = 1200;
    public static final double FIGHT_PANE_HEIGHT = 700;

    public static final double CREATURE_IMAGE_WIDTH = 60;
    public static final double CREATURE_IMAGE_HEIGHT = 63;
    public static final int BAR_HEIGHT = 10;
    public static final int FRAME_TIME = 50;

    public static final double BULLET_SPEED = 15;
    public static final double BULLET_CIRCLE_RADIUS = 5;

    public static final double START_POSITION_WIDTH = 75;
    public static final double START_POSITION_HEIGHT = 70;

    public static final class Direction {
        public static final int UP = 0;
        public static final int DOWN = 1;
        public static final int LEFT = 2;
        public static final int RIGHT = 3;
        public static final int STOP = 4;
    }

    public static final class CampType {
        public static final String GOURD = "GOURD";
        public static final String MONSTER = "MONSTER";
    }

    //对第一优先级追杀10s
    public static final int FIRST_GENERATION_AI_COUNT_TIME = 10;

    public static final int REMOTE_BULLET_TYPE = 0;
    public static final int CLOSE_BULLET_TYPE = 1;

    public static final class ClawType {
        public static final int FIRST_CLAW = 0;
        public static final int SECOND_CLAW = 1;
        public static final int THIRD_CLAW = 2;
        public static final int FOURTH_CLAW = 3;
        public static final int NONE_CLAW = 4;
    }

    public static int CLAW_IMAGE_EXIST_TIME = 200;

    public static final class EquipmentType {
        public static final int JADE_BAR_ID = 0;
        public static final int JADE_HAIRPIN_ID = 1;
        public static final int MAGIC_MIRROR_ID = 2;
        public static final int RIGID_SOFT_SWORD_ID = 3;
        public static final int TREASURE_BAG_ID = 4;
    }

    public static final long DIRECTION_LOCK_TIME = 500;//ms

    public static final class CreatureState {
        public static final int NONE_STATE = 0;
    }

    public static final class gameOverState{
        public static final int VICTORY_ID = 1;
        public static final int DEFEAT_ID = 0;
    }
}
