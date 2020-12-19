package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Grandpa extends Creature {

    public Grandpa(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.GOURD, CreatureId.GRANDPA_ID, CreatureId.GRANDPA_NAME,
                2000, 200, 75, 10, 0.5, 12, 300.0,
                faceDirection, 70.0, false, Constant.ClawType.NONE_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.GRANDPA_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.GRANDPA_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.GRANDPA_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.GRANDPA_ID));
    }
}
