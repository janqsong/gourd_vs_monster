package com.sjq.gourd.bullet;

import com.sjq.gourd.collision.Collision;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.creature.CreatureClass;
import com.sjq.gourd.creature.ImagePosition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.checkerframework.checker.units.qual.C;

public class Bullet {
    private CreatureClass sourceCreature;
    private CreatureClass targetCreature;
    private ImagePosition imagePosition;
    private Circle circleShape;
    private static final double SPEED = Constant.BULLET_SPEED;
    private boolean valid;

    public Bullet(CreatureClass sourceCreature, CreatureClass targetCreature,
                  ImagePosition imagePosition, Circle circleShape) {
        this.sourceCreature = sourceCreature;
        this.targetCreature = targetCreature;
        this.imagePosition = imagePosition;
        this.circleShape = circleShape;
        this.valid = false;
    }

    public void changeBullet(Bullet bullet) {
        this.sourceCreature = bullet.getSourceCreature();
        this.targetCreature = bullet.getTargetCreature();
        this.imagePosition = bullet.getPosXY();
        this.valid = true;
    }

    public CreatureClass getSourceCreature() {
        return sourceCreature;
    }

    public CreatureClass getTargetCreature() {
        return targetCreature;
    }

    public ImagePosition getPosXY() {
        return imagePosition;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setVisible(boolean visible) {
        circleShape.setVisible(visible);
    }

    public void draw() {
        if (!targetCreature.isAlive()) {
            circleShape.setVisible(false);
            valid = false;
            return;
        }
        if (valid) {
            circleShape.setVisible(true);
            circleShape.setCenterX(imagePosition.getLayoutX());
            circleShape.setCenterY(imagePosition.getLayoutY());
            if (sourceCreature.getCampType().equals(Constant.CampType.MONSTER))
                circleShape.setFill(Color.RED);
            else
                circleShape.setFill(Color.DODGERBLUE);
        }
    }

    public Collision move() {
        ImagePosition targetPos = targetCreature.getCenterPos();
        double deltaX = targetPos.getLayoutX() - imagePosition.getLayoutX();
        double deltaY = targetPos.getLayoutY() - imagePosition.getLayoutY();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double sin = deltaY / distance;
        double cos = deltaX / distance;
        imagePosition.setLayoutX(imagePosition.getLayoutX() + cos * SPEED);
        imagePosition.setLayoutY(imagePosition.getLayoutY() + sin * SPEED);

        if(targetPos.getLayoutX() < imagePosition.getLayoutX() &&
            imagePosition.getLayoutX() <= targetPos.getLayoutX() + targetCreature.getImageWidth() &&
            targetPos.getLayoutY() <= imagePosition.getLayoutY() &&
            imagePosition.getLayoutY() <= targetPos.getLayoutY() + targetCreature.getImageHeight()) {
            valid = false;
            return new Collision(this);
        }
        return null;
    }
}
