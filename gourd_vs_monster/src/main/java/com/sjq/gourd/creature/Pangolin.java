package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Pangolin extends Creature {

    public Pangolin(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.GOURD, CreatureId.PANGOLIN_ID, CreatureId.PANGOLIN_NAME,
                3000, 100, 75, 60, 0.5, 12, 100.0,
                faceDirection, 70.0, true, Constant.ClawType.FIRST_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.PANGOLIN_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.PANGOLIN_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.PANGOLIN_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.PANGOLIN_ID));
    }
}
