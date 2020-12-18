package com.sjq.gourd.equipment;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.creature.ImagePosition;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class EquipmentFactory {
    Random random = new Random(System.currentTimeMillis());
    ArrayList<ImageView> imageViewArrayList;
    private static int count = 0;

    public EquipmentFactory(ArrayList<ImageView> imageViews) {
        imageViewArrayList = imageViews;
    }

    public boolean hasNext() {

        return true;
    }

    public Equipment next() {
        int rand = random.nextInt(5);
        switch (rand) {
            case Constant.EquipmentType.JADE_BAR_ID: {
                break;
            }
            case Constant.EquipmentType.JADE_HAIRPIN_ID: {
                ;
                break;
            }
            case Constant.EquipmentType.MAGIC_MIRROR_ID: {
                ;
                ;
                break;
            }
            case Constant.EquipmentType.RIGID_SOFT_SWORD_ID: {
                ;
                ;
                ;
                break;
            }
            case Constant.EquipmentType.TREASURE_BAG_ID: {
                return new TreasureBag("treasureBag", count++, findFreeImageView());
            }
            default:
                break;
        }
        return null;
    }

    private ImageView findFreeImageView() {
        for(ImageView imageView:imageViewArrayList){
            if(imageView.isVisible()&&imageView.isDisable()){
                return imageView;
            }
        }
        throw new NullPointerException("EquipmentFactory ImageView不够");
    }
}
