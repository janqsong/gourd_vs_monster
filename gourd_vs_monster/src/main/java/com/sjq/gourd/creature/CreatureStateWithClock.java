package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;

public class CreatureStateWithClock {
    private CreatureState creatureState;
    //time是毫秒
    private long count = 0;

    public CreatureStateWithClock(CreatureState creatureState, long time) {
        this.creatureState = creatureState;
        this.count = time / Constant.FRAME_TIME;
    }

    public void update() {
        count--;
    }

    public boolean isOver() {
        return count <= 0;
    }

    public CreatureState getCreatureState() {
        return creatureState;
    }

    //消亡的时候执行
    public void dispose() {

    }
}
