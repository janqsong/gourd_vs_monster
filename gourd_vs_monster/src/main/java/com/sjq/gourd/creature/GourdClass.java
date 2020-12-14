package com.sjq.gourd.creature;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class GourdClass extends CreatureClass {

    public GourdClass(DataInputStream in, DataOutputStream out,
                      String campType, int creatureId, String creatureName,
                      int baseHealth, int baseMagic, int baseAttack, int baseDefense, int baseAttackSpeed,
                      int baseMoveSpeed, double shootRange, int faceDirection,
                      Image creatureLeftImage, Image selectCreatureLeftImage,
                      Image creatureRightImage, Image selectCreatureRightImage) {
        super(in, out, campType, creatureId, creatureName, baseHealth, baseMagic, baseAttack, baseDefense, baseAttackSpeed,
        baseMoveSpeed, shootRange, faceDirection, creatureLeftImage, selectCreatureLeftImage, creatureRightImage, selectCreatureRightImage);
    }
}
