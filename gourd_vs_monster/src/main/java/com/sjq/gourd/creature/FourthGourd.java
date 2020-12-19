package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class FourthGourd extends Creature {

    public FourthGourd(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.GOURD, CreatureId.FOURTH_GOURD_ID, CreatureId.FOURTH_GOURD_NAME,
                2500, 150, 80, 30, 0.5, 12, 400.0,
                faceDirection, 70.0, false, Constant.ClawType.NONE_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.FOURTH_GOURD_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.FOURTH_GOURD_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.FOURTH_GOURD_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.FOURTH_GOURD_ID));
    }
}
