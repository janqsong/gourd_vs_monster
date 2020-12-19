package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class FirstGourd extends Creature {

    public FirstGourd(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.GOURD, CreatureId.FIRST_GOURD_ID, CreatureId.FIRST_GOURD_NAME,
                3500, 100, 120, 40, 0.5, 10, 80.0,
                faceDirection, 80.0, true, Constant.ClawType.FIRST_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.FIRST_GOURD_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.FIRST_GOURD_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.FIRST_GOURD_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.FIRST_GOURD_ID));
    }
}
