package com.sjq.gourd.creature;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.protocol.CreatureStateMsg;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.checkerframework.framework.qual.QualifierArgument;

import java.io.*;
import java.util.ArrayList;

public class SecondGourd extends Creature {

    private boolean inQAction = false;
    private long gap = 5000; //ms
    private long lastQActionMillis = 0;

    ObjectOutputStream out = null;

    public SecondGourd(ObjectOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(Constant.CampType.GOURD, CreatureId.SECOND_GOURD_ID, CreatureId.SECOND_GOURD_NAME,
                3000, 150, 80, 35, 0.5, 10, 400.0,
                faceDirection, 70.0, false, Constant.ClawType.NONE_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.SECOND_GOURD_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.SECOND_GOURD_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.SECOND_GOURD_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.SECOND_GOURD_ID));
        this.out = out;
    }

    @Override
    //封装移动方式,画,攻击,返回子弹
    public ArrayList<Bullet> update() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        if (!isControlled()) {
            if (isAlive()) {
//                setCreatureState();这东西在move里更新就能保证
                aiInterface.moveMod(this, enemyFamily);
                draw();
                Bullet bullet = aiInterface.aiAttack(this, enemyFamily);
                if (bullet != null)
                    bullets.add(bullet);
                if (inQAction && (double) System.currentTimeMillis() - lastQActionMillis > gap)
                    disposeQAction();
            } else {
                draw();
            }
        } else {
            draw();
            Bullet bullet = playerAttack();
            if (bullet != null)
                bullets.add(bullet);
            if (qFlag && !inQAction) {
                //保证两次技能不重叠
                qAction();
            }
            qFlag = false;
            if (inQAction && (double) System.currentTimeMillis() - lastQActionMillis > 4 * gap)
                disposeQAction();
        }
        return bullets;
    }

    @Override
    public ArrayList<Bullet> qAction() {
        //老二的技能:兄弟们冲啊！给2.0倍攻速范围内所有友军增加振奋buff,20s内最多使用一次
        ArrayList<Bullet> bullets = new ArrayList<>();
        if (currentMagic < baseMagic)
            return bullets;
        currentMagic = 0;
        for (Creature creature : myFamily.values()) {
            if (this.imagePosition.getDistance(creature.imagePosition) < 2 * shootRange)
                new CreatureStateMsg(creature.getCampType(), creature.getCreatureId(),
                        CreatureState.STIMULATED.ordinal(), gap).sendMsg(out);
//                creature.addState(new CreatureStateWithClock(CreatureState.STIMULATED, gap));
        }
        inQAction = true;
        lastQActionMillis = System.currentTimeMillis();
        addState(new CreatureStateWithClock(CreatureState.Q_ACTION, 20000));
        return bullets;
    }

    private void disposeQAction() {
        inQAction = false;
    }
}
