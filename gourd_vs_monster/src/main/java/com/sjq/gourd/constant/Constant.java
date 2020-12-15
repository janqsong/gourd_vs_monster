package com.sjq.gourd.constant;

public class Constant {
    public static final int STAGE_WIDTH = 1360;
    public static final int STAGE_HEIGHT = 740;

    public static final double FIGHT_PANE_WIDTH = 1200;
    public static final double FIGHT_PANE_HEIGHT = 700;

    public static final int CREATURE_IMAGE_WIDTH = 60;
    public static final int BAR_HEIGHT = 10;

    public static final double BULLET_SPEED = 15;

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
        public static final String GOURD = "Gourd";
        public static final String MONSTER = "MONSTER";
    }
}
