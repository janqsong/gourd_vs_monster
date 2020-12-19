package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ToadMonster extends Creature{
    public ToadMonster(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.MONSTER, CreatureId.MONSTER4_ID, CreatureId.MONSTER4_NAME,
                3500, 100, 75, 40, 0.5, 10, 500.0,
                faceDirection, 70.0, false, Constant.ClawType.NONE_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.MONSTER4_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.MONSTER4_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.MONSTER4_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.MONSTER4_ID));
    }
}
