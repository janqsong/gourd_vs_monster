package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SnakeMonster extends Creature {

    public SnakeMonster(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.MONSTER, CreatureId.SNAKE_MONSTER_ID, CreatureId.SNAKE_MONSTER_NAME,
                4000, 200, 120, 30, 0.7, 15, 500.0,
                faceDirection, 90.0, false, Constant.ClawType.NONE_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.SNAKE_MONSTER_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.SNAKE_MONSTER_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.SNAKE_MONSTER_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.SNAKE_MONSTER_ID));
    }
}
