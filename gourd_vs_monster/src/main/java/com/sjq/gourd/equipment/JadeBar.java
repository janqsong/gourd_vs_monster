package com.sjq.gourd.equipment;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.creature.Creature;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class JadeBar extends Equipment {
    private final double moveSpeedIncrement = 8;
    private final double attackIncrement = 20;
    private final double defenseIncrement = 20;

    private static Image image = new Image("/images/equipmentImages/jadeBar.png");
    private static final double width = 50, height = 50;

    JadeBar(int id, ImageView imageView) {
        super("JadeBar", id, image, imageView, width, height);
    }

    //如意的效果葫芦娃和蛇精都可以使用,对葫芦娃的作用是增加移速,增加攻击力
    //对蛇精的作用是增加防御力,并在拾取瞬间加大管血
    @Override
    public void takeEffect(Creature creature) {
        if (creature.getCampType().equals(Constant.CampType.GOURD)) {
            creature.setCurrentMoveSpeed(creature.getCurrentMoveSpeed() + moveSpeedIncrement);
            creature.setCurrentAttack(creature.getCurrentAttack() + attackIncrement);
        } else {
            creature.setCurrentHealth(creature.getCurrentHealth() + 1000);
            creature.setCurrentDefense(creature.getCurrentDefense() + defenseIncrement);
        }
    }

    @Override
    public void giveUpTakeEffect(Creature creature) {
        if (creature.getCampType().equals(Constant.CampType.GOURD)) {
            creature.setCurrentMoveSpeed(creature.getCurrentMoveSpeed() - moveSpeedIncrement);
            creature.setCurrentAttack(creature.getCurrentAttack() - attackIncrement);
        } else {
            creature.setCurrentDefense(creature.getCurrentDefense() - defenseIncrement);
        }
    }
}
