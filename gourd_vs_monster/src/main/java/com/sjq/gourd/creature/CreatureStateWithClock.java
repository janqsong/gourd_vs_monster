package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;

public class CreatureStateWithClock {
    private final CreatureState creatureState;
    //time是毫秒
    private long lastMillis = 0;
    private long gap = 0;
    private long flag = 0;

    public CreatureStateWithClock(CreatureState creatureState, long time) {
        this.creatureState = creatureState;
        this.gap = time;
        this.lastMillis = System.currentTimeMillis();
    }

    public void update() {
        flag = System.currentTimeMillis() - lastMillis;
    }

    public boolean isOver() {
        return flag >= gap;
    }

    public CreatureState getCreatureState() {
        return creatureState;
    }

    //消亡的时候执行
    public void dispose() {

    }

    public long getGap() {
        return gap;
    }

    public void setGap(long gap) {
        this.gap = gap;
        //setGap是重置了这个buff,意味着这个buff的开始时间重置
        lastMillis = System.currentTimeMillis();
    }

    public long getRemainTime() {
        return lastMillis + gap - System.currentTimeMillis();
    }
}
