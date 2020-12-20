package com.sjq.gourd.creature;

public enum CreatureState {
    NONE, SPEED_CUT, FROZEN, FIRING, IMPRISONMENT,SERIOUS_INJURY,
    STIMULATED, CURE,
}

// SPEED_CUT 减速50%
// FROZEN 完全暂停1s
// FIRING 灼烧,持续每一帧掉血50
// IMPRISONMENT 禁锢,无法移动,但是可以攻击,只要距离够
// SERIOUS_INJURY 重伤


// STIMULATED 振奋,移速提升20%,攻速提升20%,持续5秒
// CURE 回血,每一帧回复一定血量
