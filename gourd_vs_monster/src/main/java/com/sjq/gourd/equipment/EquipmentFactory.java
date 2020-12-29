package com.sjq.gourd.equipment;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.creature.ImagePosition;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class EquipmentFactory {
    Random random = new Random(System.currentTimeMillis());
    ArrayList<ImageView> imageViewArrayList;
    boolean imageViewFlag = false;//标记imageView用完了
    private static int count = 0;
    private int frameCount = 0;
    private final int timeGap = 10; //10s一个装备

    public EquipmentFactory(ArrayList<ImageView> imageViews) {
        imageViewArrayList = imageViews;
    }

    public boolean hasNext() {
        frameCount++;
        if (frameCount >= timeGap * 1000 / Constant.FRAME_TIME && !imageViewFlag) {
            frameCount = 0;
            return true;
        }
        return false;
    }

    public Equipment next() {
        ImageView imageView = findFreeImageView();
        if (imageView == null)
            return null;

        Equipment equipment = null;
        int rand = random.nextInt(5);
        switch (rand) {
            case Constant.EquipmentType.JADE_BAR_ID: {
                equipment = new JadeBar(count++, imageView);
                break;
            }
            case Constant.EquipmentType.JADE_HAIRPIN_ID: {
                equipment = new JadeHairpin(count++, imageView);
                break;
            }
            case Constant.EquipmentType.MAGIC_MIRROR_ID: {
                equipment = new MagicMirror(count++, imageView);
                break;
            }
            case Constant.EquipmentType.RIGID_SOFT_SWORD_ID: {
                equipment = new RigidSoftSword(count++, imageView);
                break;
            }
            case Constant.EquipmentType.TREASURE_BAG_ID: {
                equipment = new TreasureBag(count++, imageView);
                break;
            }
            default:
                break;
        }
        return equipment;
    }

    private ImageView findFreeImageView() {
        ImageView tempImageView = null;
        for (ImageView imageView : imageViewArrayList) {
            if (!imageView.isVisible()) {
                if(tempImageView == null) {
                    tempImageView = imageView;
                } else {
                    imageViewFlag = false;
                    return tempImageView;
                }
            }
        }
        imageViewFlag = true;
        return tempImageView;
    }

    public int nextInt() {
        return random.nextInt(5);
    }

    public ImagePosition nextImagePosition() {
        return new ImagePosition(random.nextInt((int) (Constant.FIGHT_PANE_WIDTH)),
                random.nextInt((int) (Constant.FIGHT_PANE_HEIGHT)));
    }

    public Equipment next(int rand, ImagePosition imagePosition) {
        ImageView imageView = findFreeImageView();
        if (imageView == null)
            return null;

        Equipment equipment = null;
        switch (rand) {
            case Constant.EquipmentType.JADE_BAR_ID: {
                equipment = new JadeBar(count++, imageView);
                break;
            }
            case Constant.EquipmentType.JADE_HAIRPIN_ID: {
                equipment = new JadeHairpin(count++, imageView);
                break;
            }
            case Constant.EquipmentType.MAGIC_MIRROR_ID: {
                equipment = new MagicMirror(count++, imageView);
                break;
            }
            case Constant.EquipmentType.RIGID_SOFT_SWORD_ID: {
                equipment = new RigidSoftSword(count++, imageView);
                break;
            }
            case Constant.EquipmentType.TREASURE_BAG_ID: {
                equipment = new TreasureBag(count++, imageView);
                break;
            }
            default:
                break;
        }
        if (equipment != null) {
            equipment.setImagePosition(imagePosition);
        }
        return equipment;
    }
}
