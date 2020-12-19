package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SixthGourd extends Creature {

    public SixthGourd(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.GOURD, CreatureId.SIXTH_GOURD_ID, CreatureId.SIXTH_GOURD_NAME,
                2500, 100, 150, 20, 0.5, 12, 90.0,
                faceDirection, 70.0, true, Constant.ClawType.FIRST_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.SIXTH_GOURD_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.SIXTH_GOURD_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.SIXTH_GOURD_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.SIXTH_GOURD_ID));
    }
}
