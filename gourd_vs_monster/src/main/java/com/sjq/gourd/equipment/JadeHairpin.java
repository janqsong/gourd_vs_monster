package com.sjq.gourd.equipment;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.creature.Creature;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class JadeHairpin extends Equipment {
    private final double moveSpeedIncrement = 5;
    private double attackSpeed = 0;
    private final double defenseIncrement = 20;
    private final double attackIncrement = 20;


    private static Image image = new Image("/images/equipmentImages/jadeHairpin.png");
    private static final double width = 50, height = 50;

    JadeHairpin(int id, ImageView imageView) {
        super("jadeHairpin", id, image, imageView, width, height);
    }


    //只有蛇精捡才有效,其他生物可以捡,但是无法装备
    //蛇精捡了回复1500血,增加移速,攻速200%基础攻速,防御力,攻击力
    @Override
    public void takeEffect(Creature creature) {
        if (creature.getCreatureId() == CreatureId.SNAKE_MONSTER_ID) {
            creature.setCurrentMoveSpeed(creature.getCurrentMoveSpeed() + moveSpeedIncrement);
            attackSpeed = 2.0 * creature.getBaseAttackSpeed();
            creature.setCurrentAttackSpeed(creature.getCurrentAttackSpeed() + attackSpeed);
            creature.setCurrentDefense(creature.getCurrentDefense() + defenseIncrement);
            creature.setCurrentAttack(creature.getCurrentAttack() + attackIncrement);
        }
    }

    @Override
    public void giveUpTakeEffect(Creature creature) {
        if (creature.getCreatureId() == CreatureId.SNAKE_MONSTER_ID) {
            creature.setCurrentMoveSpeed(creature.getCurrentMoveSpeed() - moveSpeedIncrement);
            attackSpeed = 2.0 * creature.getBaseAttackSpeed();
            creature.setCurrentAttackSpeed(creature.getCurrentAttackSpeed() - attackSpeed);
            creature.setCurrentDefense(creature.getCurrentDefense() - defenseIncrement);
            creature.setCurrentAttack(creature.getCurrentAttack() - attackIncrement);
        }
    }
}
