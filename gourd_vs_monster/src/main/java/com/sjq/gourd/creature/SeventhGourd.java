package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SeventhGourd extends Creature {

    public SeventhGourd(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.GOURD, CreatureId.SEVENTH_GOURD_ID, CreatureId.SEVENTH_GOURD_NAME,
                2500, 100, 80, 20, 0.9, 15, 600.0,
                faceDirection, 70.0, false, Constant.ClawType.NONE_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.SEVENTH_GOURD_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.SEVENTH_GOURD_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.SEVENTH_GOURD_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.SEVENTH_GOURD_ID));
    }
}
