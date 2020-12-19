package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class BatMonster extends Creature{
    public BatMonster(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.MONSTER, CreatureId.MONSTER2_ID, CreatureId.MONSTER2_NAME,
                2500, 100, 70, 15, 1.0, 18, 600.0,
                faceDirection, 70.0, false, Constant.ClawType.NONE_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.MONSTER2_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.MONSTER2_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.MONSTER2_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.MONSTER2_ID));
    }
}
