package com.sjq.gourd.creature;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CreatureClass {
    protected String creatureName;
    protected int healthValue;
    protected int attackValue;
    protected int defenseValue;
    protected int attackSpeed;
    protected int moveSpeed;
    protected ImageView creatureImageView = new ImageView();
    protected String creatureImageUrl;
    protected String selectCreatureImageUrl;

    public CreatureClass(String creatureName) {
        this.creatureName = creatureName;
    }

    public void addCreatureImageView(ImageView creatureImageView) {
        this.creatureImageView = creatureImageView;
    }

    public void addCreatureImageUrl(String creatureImageUrl) {
        this.creatureImageUrl = creatureImageUrl;
    }

    public void setCreatureImageView() {
        this.creatureImageView.setImage(new Image(creatureImageUrl));
    }

    public void addSelectCreatureImageUrl(String selectCreatureImageUrl) {
        this.selectCreatureImageUrl = selectCreatureImageUrl;
    }

    public void setSelectCreatureImageView() {
        this.creatureImageView.setImage(new Image(selectCreatureImageUrl));
    }

    public ImageView getCreatureImageView() {
        return creatureImageView;
    }

    public String getCreatureId() { return creatureName; }
}
