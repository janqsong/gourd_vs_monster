package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ThirdGourd extends Creature {

    public ThirdGourd(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.GOURD, CreatureId.THIRD_GOURD_ID, CreatureId.THIRD_GOURD_NAME,
                3000, 100, 150, 60, 0.4, 10, 80.0,
                faceDirection, 70.0, true, Constant.ClawType.FIRST_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.THIRD_GOURD_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.THIRD_GOURD_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.THIRD_GOURD_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.THIRD_GOURD_ID));
    }
}
