package com.sjq.gourd.equipment;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.creature.ImagePosition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Random;

public abstract class Equipment {
    protected ImagePosition imagePosition;
    protected String name;
    protected Image equipmentImage = null;
    protected int id;
    protected ImageView imageView;
    protected Creature myCreature = null;
    protected boolean isFree = false;
    private final double WIDTH, HEIGHT;
    private Random random = new Random(System.currentTimeMillis());

    Equipment(String name, int id, Image image, ImageView imageView, double width, double height) {
        this.name = name;
        this.id = id;
        this.equipmentImage = image;
        this.imageView = imageView;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.isFree = true;
        imagePosition = new ImagePosition(random.nextInt((int) (Constant.FIGHT_PANE_WIDTH - width)),
                random.nextInt((int) (Constant.FIGHT_PANE_HEIGHT - height)));
        this.imageView.setImage(image);
        this.imageView.setFitWidth(width);
        this.imageView.setFitHeight(height);
    }

    public void draw() {
        if (isFree) {
            imageView.setLayoutX(imagePosition.getLayoutX());
            imageView.setLayoutY(imagePosition.getLayoutY());
            imageView.setVisible(true);
            imageView.setDisable(false);
        } else {
            imageView.setVisible(false);
            imageView.setDisable(true);
        }
    }

    public void dispose(){
        imageView.setVisible(false);
        imageView.setDisable(true);
    }

    public abstract void takeEffect(Creature creature);

    public void setImagePosition(ImagePosition imagePosition) {
        this.imagePosition = imagePosition;
    }

    public double getWIDTH() {
        return WIDTH;
    }

    public double getHEIGHT() {
        return HEIGHT;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
