package com.sjq.gourd.equipment;

import com.sjq.gourd.creature.CreatureClass;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class JadeBar extends Equipment{

    JadeBar(String name, int id, Image image, ImageView imageView, double width, double height) {
        super(name, id, image, imageView, width, height);
    }

    @Override
    public void takeEffect(CreatureClass creatureClass) {

    }
}
