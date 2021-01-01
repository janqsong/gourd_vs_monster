package com.ttf.gourd.equipment;

import com.ttf.gourd.creature.Creature;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TreasureBag extends Equipment {
    private static final Image image = new Image("/images/equipmentImages/treasureBag.png");
    private static final double width = 50, height = 50;

    TreasureBag(int id, ImageView imageView) {
        super("TreasureBag", id, image, imageView, width, height);
    }

    @Override
    public void takeEffect(Creature creature) {
        creature.setCurrentHealth(creature.getCurrentHealth() + 500);
    }

    @Override
    public void giveUpTakeEffect(Creature creature) {
        //因为不会装备在身上,所以也不会丢弃,此函数不生效
    }
}
