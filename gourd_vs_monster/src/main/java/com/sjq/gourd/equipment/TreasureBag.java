package com.sjq.gourd.equipment;

import com.sjq.gourd.creature.CreatureClass;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TreasureBag extends Equipment {
    private static Image image = new Image("/images/equipmentImages/treasureBag.png");
    private static final double width = 50, height = 50;

    TreasureBag(String name, int id, ImageView imageView) {
        super(name, id, image, imageView, width, height);
    }

    @Override
    public void takeEffect(CreatureClass creatureClass) {
        creatureClass.setCurrentHealth(creatureClass.getCurrentHealth() + 500);
    }
}
