package com.ttf.gourd.creature;

import com.ttf.gourd.constant.Constant;
import com.ttf.gourd.constant.CreatureId;
import com.ttf.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

public class ToadMonster extends Creature {
    Creature[] creatures = new Creature[4];

    ToadMonster(int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(Constant.CampType.MONSTER, CreatureId.MONSTER4_ID, CreatureId.MONSTER4_NAME,
                3500, 100, 75, 40, 0.5, 10, 500.0,
                faceDirection, 70.0, false, Constant.ClawType.NONE_CLAW,
                imageView, closeAttackImageView, ImageUrl.monsterLeftImageMap.get(CreatureId.MONSTER4_ID),
                ImageUrl.monsterLeftSelectImageMap.get(CreatureId.MONSTER4_ID),
                ImageUrl.monsterRightImageMap.get(CreatureId.MONSTER4_ID),
                ImageUrl.monsterRightSelectImageMap.get(CreatureId.MONSTER4_ID));
    }


    @Override
    public void rAction() {
        //女王陛下手下我的膝盖,四人全死,复活蛇精
        boolean flag = canRAction();
        if (flag) {
            for (int i = 0; i < 4; i++) {
                creatures[i].setCurrentMagic(0);
                creatures[i].setCurrentHealth(0);
            }
            Creature creature = myFamily.get(CreatureId.SNAKE_MONSTER_ID);
            if (creature != null) {
                creature.setCurrentHealth(creature.baseHealth);
                creature.setCurrentMagic(creature.baseMagic);
            }
        }
    }

    private boolean canRAction() {
        Creature creature = myFamily.get(CreatureId.SNAKE_MONSTER_ID);
        if (creature == null || creature.currentHealth > 0)
            return false;
        creatures[0] = myFamily.get(CreatureId.MONSTER1_ID);
        creatures[1] = myFamily.get(CreatureId.MONSTER2_ID);
        creatures[2] = myFamily.get(CreatureId.MONSTER3_ID);
        creatures[3] = myFamily.get(CreatureId.MONSTER4_ID);
        for (int i = 0; i < 4; i++) {
            if (creatures[i] == null)
                return false;
            if (!creatures[i].isAlive())
                return false;
            if (creatures[i].currentMagic < creatures[i].baseHealth)
                return false;
        }
        return true;
    }
}
