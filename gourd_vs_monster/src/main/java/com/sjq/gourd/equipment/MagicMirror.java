package com.sjq.gourd.equipment;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.creature.Creature;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MagicMirror extends Equipment {
    private final double shootRangeIncrement = 200;

    private static final Image image = new Image("/images/equipmentImages/magicMirror.png");
    private static final double width = 50, height = 50;

    MagicMirror(int id, ImageView imageView) {
        super("magicMirror", id, image, imageView, width, height);
    }

    //魔镜的拾取,可以增加远程攻击的攻击距离300
    //葫芦娃拾取会增加一定血量
    //妖怪获得则给子弹附加魔镜的凝视效果,使得被攻击者处于重伤状态,回血效果大减,并有效遏制葫芦娃技能
    @Override
    public void takeEffect(Creature creature) {
        if (!creature.isCloseAttack())
            creature.setShootRange(creature.getShootRange() + shootRangeIncrement);
        if (creature.getCampType().equals(Constant.CampType.GOURD)) {
            creature.setCurrentHealth(creature.getCurrentHealth() + 500);
        }
    }

    @Override
    public void giveUpTakeEffect(Creature creature) {
        if (!creature.isCloseAttack())
            creature.setShootRange(creature.getShootRange() - shootRangeIncrement);
    }
}
