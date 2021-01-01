package com.ttf.gourd.bullet;

import com.ttf.gourd.collision.Collision;
import com.ttf.gourd.constant.Constant;
import com.ttf.gourd.constant.CreatureId;
import com.ttf.gourd.creature.Creature;
import com.ttf.gourd.creature.ImagePosition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Bullet {
    private final Creature sourceCreature;
    private final Creature targetCreature;
    private final ImagePosition imagePosition;
    private final Circle circleShape;
    private final int bulletType;
    private static final double SPEED = Constant.BULLET_SPEED;
    private boolean valid;
    private BulletState bulletState = BulletState.NONE;

    public Bullet(Creature sourceCreature, Creature targetCreature, int bulletType, BulletState bulletState) {
        this.sourceCreature = sourceCreature;
        this.targetCreature = targetCreature;
        this.bulletType = bulletType;
        this.bulletState = bulletState;
        this.valid = true;
        this.imagePosition = this.sourceCreature.getCenterPos();
        if (this.bulletType == Constant.CLOSE_BULLET_TYPE)
            this.circleShape = null;//近战没有图形
        else {
            this.circleShape = new Circle(imagePosition.getLayoutX(), imagePosition.getLayoutY(), 5);
            this.circleShape.setVisible(false);
            int id = sourceCreature.getCreatureId();
            if (id == CreatureId.SNAKE_MONSTER_ID) {
                this.circleShape.setFill(Color.BLACK);
                this.circleShape.setRadius(7);
            } else if (id == CreatureId.MONSTER2_ID) {
                //蝙蝠的
                this.circleShape.setFill(Color.ROSYBROWN);
                this.circleShape.setRadius(6);
            } else if (id == CreatureId.MONSTER4_ID) {
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

            if (this.bulletState.equals(BulletState.THE_SON_OF_FLAME)
                    || this.bulletState.equals(BulletState.THE_HEART_OF_ICE)
                    || this.bulletState.equals(BulletState.THE_GOD_OF_HEALING_MAX)
                    || this.bulletState.equals(BulletState.THE_TEETH_OF_POISONOUS))
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


    //设置子弹可见状态
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

    //设置子弹位置
    public void setImagePosition(double layoutX, double layoutY) {
        imagePosition.setLayoutX(layoutX);
        imagePosition.setLayoutY(layoutY);
    }

    //根据子弹当前位置绘制子弹
    public void draw() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //目标死亡,子弹消失,无效
                if (!targetCreature.isAlive()) {
                    circleShape.setVisible(false);
                    valid = false;
                    return;
                }
                //子弹有效,绘画,无效,不绘制
                if (valid) {
                    circleShape.setVisible(true);
                    circleShape.setCenterX(imagePosition.getLayoutX());
                    circleShape.setCenterY(imagePosition.getLayoutY());
                } else
                    circleShape.setVisible(false);
            }
        });
    }

    //子弹移动返回碰撞
    private Collision move() {
        //获取目标中心位置
        ImagePosition targetPos = targetCreature.getCenterPos();
        double deltaX = targetPos.getLayoutX() - imagePosition.getLayoutX();
        double deltaY = targetPos.getLayoutY() - imagePosition.getLayoutY();
        //根据偏移量计算角度
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double sin = deltaY / distance;
        double cos = deltaX / distance;
        //根据速度和角度设置新的子弹位置
        imagePosition.setLayoutX(imagePosition.getLayoutX() + cos * SPEED);
        imagePosition.setLayoutY(imagePosition.getLayoutY() + sin * SPEED);

        //子弹和目标图形相交发生碰撞
        if (circleShape.intersects(targetCreature.getCreatureImageView().getBoundsInParent())) {
            valid = false;
            return new Collision(this);
        }
        return null;
    }

    //子弹更新,返回碰撞,无碰撞返回null
    public Collision update() {
        //远程子弹绘制并返回碰撞,近战子弹直接返回碰撞
        if (bulletType == Constant.REMOTE_BULLET_TYPE) {
            draw();
            return move();
        } else
            return new Collision(this);
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
