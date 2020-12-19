package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class CentipedeMonster extends Creature{
    public CentipedeMonster(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.MONSTER, CreatureId.MONSTER1_ID, CreatureId.MONSTER1_NAME,
                3000, 100, 75, 20, 0.5, 8, 80.0,
                faceDirection, 90.0, true, Constant.ClawType.FOURTH_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.MONSTER1_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.MONSTER1_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.MONSTER1_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.MONSTER1_ID));
    }
}
