package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class CrocodileMonster extends Creature{
    public CrocodileMonster(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.MONSTER, CreatureId.MONSTER3_ID, CreatureId.MONSTER3_NAME,
                3500, 100, 100, 40, 0.5, 8, 80.0,
                faceDirection, 80.0, true, Constant.ClawType.SECOND_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.MONSTER3_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.MONSTER3_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.MONSTER3_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.MONSTER3_ID));
    }
}
