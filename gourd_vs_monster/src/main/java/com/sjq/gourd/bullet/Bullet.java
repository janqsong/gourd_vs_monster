package com.sjq.gourd.bullet;

import com.sjq.gourd.collision.Collision;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.creature.ImagePosition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Bullet {
    private Creature sourceCreature;
    private Creature targetCreature;
    private ImagePosition imagePosition;
    private Circle circleShape;
    private static final double SPEED = Constant.BULLET_SPEED;
    private boolean valid;
    private int bulletType;

    public Bullet(Creature sourceCreature, Creature targetCreature,
                  ImagePosition imagePosition, Circle circleShape) {
        this.sourceCreature = sourceCreature;
        this.targetCreature = targetCreature;
        this.imagePosition = imagePosition;
        this.circleShape = new Circle(sourceCreature.getCenterPos().getLayoutX(), sourceCreature.getCenterPos().getLayoutY(), 5);
        this.circleShape.setVisible(false);
        this.valid = true;
        this.bulletType = Constant.REMOTE_BULLET_TYPE;
    }

    public Bullet(Creature sourceCreature, Creature targetCreature,
                  ImagePosition imagePosition) {
        this.sourceCreature = sourceCreature;
        this.targetCreature = targetCreature;
        this.imagePosition = imagePosition;
        this.valid = true;
        this.bulletType = Constant.CLOSE_BULLET_TYPE;
    }

    public void changeBullet(Bullet bullet) {
        this.sourceCreature = bullet.getSourceCreature();
        this.targetCreature = bullet.getTargetCreature();
        this.imagePosition = bullet.getPosXY();
        this.bulletType = bullet.bulletType;
        this.valid = true;
    }

    public Circle getCircleShape() {
        return circleShape;
    }

    public Creature getSourceCreature() {
        return sourceCreature;
    }

    public Creature getTargetCreature() {
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
       if(circleShape!=null){
           circleShape.setVisible(visible);
       }
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

        if (circleShape.intersects(targetCreature.getCreatureImageView().getBoundsInParent())) {
            valid = false;
            return new Collision(this);
        }
        return null;
    }

    public Collision update() {
        if (bulletType == Constant.REMOTE_BULLET_TYPE) {
            draw();
            Collision collision = move();
            return collision;
        } else {
            return new Collision(this);
        }
    }

    public int getBulletType() {
        return bulletType;
    }
}
