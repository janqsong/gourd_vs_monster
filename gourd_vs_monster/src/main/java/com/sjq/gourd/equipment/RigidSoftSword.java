package com.sjq.gourd.equipment;

import com.sjq.gourd.creature.Creature;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RigidSoftSword extends Equipment {
    private double attackSpeedIncrement = 0;
    private final double attackIncrement = 20;

    private static Image image = new Image("/images/equipmentImages/rigidSoftSword.png");
    private static final double width = 50, height = 50;

    RigidSoftSword(int id, ImageView imageView) {
        super("rigidSoftSword", id, image, imageView, width, height);
    }


    //增加100%基础攻速,增加攻击力
    @Override
    public void takeEffect(Creature creature) {
        attackSpeedIncrement = 1.0 * creature.getBaseAttackSpeed();
        creature.setCurrentAttackSpeed(creature.getCurrentAttackSpeed() + attackSpeedIncrement);
        creature.setCurrentAttack(creature.getCurrentAttack() + attackIncrement);
    }

    @Override
    public void giveUpTakeEffect(Creature creature) {
        creature.setCurrentAttackSpeed(creature.getCurrentAttackSpeed() - attackSpeedIncrement);
        creature.setCurrentAttack(creature.getCurrentAttack() - attackIncrement);
    }
}
