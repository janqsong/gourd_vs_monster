package com.sjq.gourd.bullet;

import com.sjq.gourd.collision.Collision;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.creature.ImagePosition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.checkerframework.checker.units.qual.C;

public class Bullet {
    private Creature sourceCreature;
    private Creature targetCreature;
    private ImagePosition imagePosition;
    private Circle circleShape;
    private static final double SPEED = Constant.BULLET_SPEED;
    private boolean valid;
    private int bulletType;
    private BulletState bulletState = BulletState.NONE;

    public Bullet(Creature sourceCreature, Creature targetCreature, int bulletType, BulletState bulletState) {
        this.sourceCreature = sourceCreature;
        this.targetCreature = targetCreature;
        this.bulletType = bulletType;
        this.bulletState = bulletState;
        this.valid = true;
        this.imagePosition = this.sourceCreature.getCenterPos();
        if(this.bulletType == Constant.CLOSE_BULLET_TYPE)
            this.circleShape = null;//近战没有图形
        else{
            this.circleShape = new Circle(imagePosition.getLayoutX(),imagePosition.getLayoutY(),5);
            this.circleShape.setVisible(false);
            int id = sourceCreature.getCreatureId();
            if (id == CreatureId.SNAKE_MONSTER_ID) {
                this.circleShape.setFill(Color.BLACK);
                this.circleShape.setRadius(7);
            } else if (id == CreatureId.MONSTER2_ID) {
                //蝙蝠的
                this.circleShape.setFill(Color.ROSYBROWN);
                this.circleShape.setRadius(6);
            }else if(id == CreatureId.MONSTER4_ID){
                //蛤蟆精的
                this.circleShape.setFill(Color.LIGHTYELLOW);
                this.circleShape.setRadius(6);
            } else if (id == CreatureId.SECOND_GOURD_ID)
                this.circleShape.setFill(Color.ORANGE);
            else if (id == CreatureId.FOURTH_GOURD_ID)
                this.circleShape.setFill(Color.RED);
            else if (id == CreatureId.FIFTH_GOURD_ID)
                this.circleShape.setFill(Color.BLUE);
            else if (id == CreatureId.SEVENTH_GOURD_ID)
                this.circleShape.setFill(Color.PURPLE);
            else if (id == CreatureId.GRANDPA_ID)
                this.circleShape.setFill(Color.GREEN);

            if(this.bulletState.equals(BulletState.THE_SON_OF_FLAME)
                    ||this.bulletState.equals(BulletState.THE_HEART_OF_ICE)
                    ||this.bulletState.equals(BulletState.THE_GOD_OF_HEALING_MAX)
                    ||this.bulletState.equals(BulletState.THE_TEETH_OF_POISONOUS))
                this.circleShape.setRadius(10);
        }
    }

//    public Bullet(Creature sourceCreature, Creature targetCreature,
//                  ImagePosition imagePosition, Circle circleShape) {
//        this.sourceCreature = sourceCreature;
//        this.targetCreature = targetCreature;
//        this.imagePosition = imagePosition;
//        this.circleShape = new Circle(sourceCreature.getCenterPos().getLayoutX(), sourceCreature.getCenterPos().getLayoutY(), 5);
//        this.circleShape.setVisible(false);
//        this.valid = true;
//        this.bulletType = Constant.REMOTE_BULLET_TYPE;
//        if (sourceCreature.getCampType().equals(Constant.CampType.GOURD))
//            this.circleShape.setFill(Color.DODGERBLUE);
//        else
//            this.circleShape.setFill(Color.RED);
//
//        int id = sourceCreature.getCreatureId();
//        if (id == CreatureId.SNAKE_MONSTER_ID) {
//            this.circleShape.setFill(Color.BLACK);
//            this.circleShape.setRadius(7);
//        } else if (id == CreatureId.MONSTER2_ID) {
//            //蝙蝠的
//            this.circleShape.setFill(Color.ROSYBROWN);
//            this.circleShape.setRadius(6);
//        }else if(id == CreatureId.MONSTER4_ID){
//            //蛤蟆精的
//            this.circleShape.setFill(Color.LIGHTYELLOW);
//            this.circleShape.setRadius(6);
//        } else if (id == CreatureId.SECOND_GOURD_ID)
//            this.circleShape.setFill(Color.ORANGE);
//        else if (id == CreatureId.FOURTH_GOURD_ID)
//            this.circleShape.setFill(Color.RED);
//        else if (id == CreatureId.FIFTH_GOURD_ID)
//            this.circleShape.setFill(Color.BLUE);
//        else if (id == CreatureId.SEVENTH_GOURD_ID)
//            this.circleShape.setFill(Color.PURPLE);
//        else if (id == CreatureId.GRANDPA_ID)
//            this.circleShape.setFill(Color.GREEN);
//    }
//
//    public Bullet(Creature sourceCreature, Creature targetCreature, double radius, Paint value, BulletState bulletState) {
//        this.sourceCreature = sourceCreature;
//        this.targetCreature = targetCreature;
//        imagePosition = sourceCreature.getCenterPos();
//        this.circleShape = new Circle(imagePosition.getLayoutX(), imagePosition.getLayoutY(), radius);
//        circleShape.setFill(value);
//        this.bulletState = bulletState;
//        this.bulletType = Constant.REMOTE_BULLET_TYPE;
//        this.valid = true;
//    }
//
//    public Bullet(Creature sourceCreature, Creature targetCreature,
//                  ImagePosition imagePosition) {
//        this.sourceCreature = sourceCreature;
//        this.targetCreature = targetCreature;
//        this.imagePosition = imagePosition;
//        this.valid = true;
//        this.bulletType = Constant.CLOSE_BULLET_TYPE;
//    }
//
//    //用于蝎子生成致残之爪
//    public Bullet(Creature sourceCreature, Creature targetCreature, BulletState bulletState, int bulletType) {
//        this.sourceCreature = sourceCreature;
//        this.targetCreature = targetCreature;
//        imagePosition = sourceCreature.getCenterPos();
//        this.bulletState = bulletState;
//        this.bulletType = bulletType;
//        this.valid = true;
//    }

    public Circle getCircleShape() {
        return circleShape;
    }

    public Creature getSourceCreature() {
        return sourceCreature;
    }

    public Creature getTargetCreature() {
        return targetCreature;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setVisible(boolean visible) {
        if (circleShape != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    circleShape.setVisible(visible);
                }
            });
        }
    }

    public void setImagePosition(double layoutX, double layoutY) {
        imagePosition.setLayoutX(layoutX);
        imagePosition.setLayoutY(layoutY);
    }

    public void draw() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (!targetCreature.isAlive()) {
                    circleShape.setVisible(false);
                    valid = false;
                    return;
                }
                if (valid) {
                    circleShape.setVisible(true);
                    circleShape.setCenterX(imagePosition.getLayoutX());
                    circleShape.setCenterY(imagePosition.getLayoutY());
                }
            }
        });
    }

    private Collision move() {
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

    public BulletState getBulletState() {
        return bulletState;
    }

    public ImagePosition getImagePosition() {
        return imagePosition;
    }
}
