package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ScorpionMonster extends Creature {

    public ScorpionMonster(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.MONSTER, CreatureId.SCORPION_MONSTER_ID, CreatureId.SCORPION_MONSTER_NAME,
                5000, 100, 150, 55, 0.5, 8, 100.0,
                faceDirection, 110.0, true, Constant.ClawType.THIRD_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.SCORPION_MONSTER_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.SCORPION_MONSTER_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.SCORPION_MONSTER_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.SCORPION_MONSTER_ID));
    }
}