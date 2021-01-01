package com.ttf.gourd.bullet;


public enum BulletState {
    /*
    * NONE 普通子弹
    * GAZE_OF_MAGIC_MIRROR 特效,魔镜的凝视,当远程攻击的妖精装备魔镜时会触发这个特效
    * THE_SON_OF_FLAME 特效,火焰之子,四娃技能可以触发这个技能
    * THE_HEART_OF_ICE 特效,冰霜之心,五娃技能可以触发这个特效
    * THE_GOD_OF_HEALING 特效,治愈之神,爷爷普通子弹可以触发这个特效
    * THE_POISON_OF_CRAZY 特效,疯狂之毒,暂时没有谁可以触发这个特效
    * THE_GOD_OF_HEALING_MAX 特效,大号治愈之神,爷爷的技能可以触发这个特效
    * THE_TEETH_OF_POISONOUS 特效,剧毒之牙,蛇精Q技能可以触发这个特效
    * SPEED_CUT_CLAW 特效,致残之爪,蝎子精会触发这个特效
    * */
    NONE, GAZE_OF_MAGIC_MIRROR, THE_SON_OF_FLAME,
    THE_HEART_OF_ICE, THE_GOD_OF_HEALING, THE_POISON_OF_CRAZY, THE_GOD_OF_HEALING_MAX,
    THE_TEETH_OF_POISONOUS, SPEED_CUT_CLAW
}
