package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import javafx.scene.image.ImageView;

import java.io.*;
import java.util.ArrayList;

public class CreatureFactory {
    private final String camp;
    private final int faceDirection;
    private final int minId;
    private final int maxId;
    private int currentId;
    //确保size至少有2*(maxId-minId+1)
    private ArrayList<ImageView> imageViews;

    public CreatureFactory(String camp, int faceDirection, ArrayList<ImageView> imageViews) throws Exception {
        this.camp = camp;
        this.faceDirection = faceDirection;
        if (camp == Constant.CampType.GOURD) {
            minId = CreatureId.MIN_GOURD_ID;
            maxId = CreatureId.MAX_GOURD_ID;
        } else if (camp == Constant.CampType.MONSTER) {
            minId = CreatureId.MIN_MONSTER_ID;
            maxId = CreatureId.MAX_MONSTER_ID;
        } else throw new Exception("CreatureFactory camp参数错误");
        currentId = minId - 1;
        this.imageViews = imageViews;
        int size = imageViews.size();
        if (size < 2 * (maxId - minId + 1))
            throw new RuntimeException("CreatureFactory imageViews过少");
    }

    public boolean hasNext() {
        return (minId <= currentId + 1 && currentId + 1 <= maxId);
    }

    public Creature next() throws Exception {
        if (!hasNext())
            throw new Exception("CreatureFactory 越界");
        currentId++;
        ImageView a = imageViews.get(2 * (currentId - minId));
        ImageView b = imageViews.get(2 * (currentId - minId) + 1);
        if (camp == Constant.CampType.GOURD) {
            if (currentId == CreatureId.FIRST_GOURD_ID)
                return new FirstGourd(faceDirection, a, b);
            if (currentId == CreatureId.SECOND_GOURD_ID)
                return new SecondGourd(faceDirection, a, b);
            if (currentId == CreatureId.THIRD_GOURD_ID)
                return new ThirdGourd(faceDirection, a, b);
            if (currentId == CreatureId.FOURTH_GOURD_ID)
                return new FourthGourd(faceDirection, a, b);
            if (currentId == CreatureId.FIFTH_GOURD_ID)
                return new FifthGourd(faceDirection, a, b);
            if (currentId == CreatureId.SIXTH_GOURD_ID)
                return new SixthGourd(faceDirection, a, b);
            if (currentId == CreatureId.SEVENTH_GOURD_ID)
                return new SeventhGourd(faceDirection, a, b);
            if (currentId == CreatureId.PANGOLIN_ID)
                return new Pangolin(faceDirection, a, b);
            if (currentId == CreatureId.GRANDPA_ID)
                return new Grandpa(faceDirection, a, b);
        } else {
            if (currentId == CreatureId.SNAKE_MONSTER_ID)
                return new SnakeMonster(faceDirection, a, b);
            if (currentId == CreatureId.SCORPION_MONSTER_ID)
                return new ScorpionMonster(faceDirection, a, b);
            if (currentId == CreatureId.MONSTER1_ID)
                return new CentipedeMonster(faceDirection, a, b);
            if (currentId == CreatureId.MONSTER2_ID)
                return new BatMonster(faceDirection, a, b);
            if (currentId == CreatureId.MONSTER3_ID)
                return new CrocodileMonster(faceDirection, a, b);
            if (currentId == CreatureId.MONSTER4_ID)
                return new ToadMonster(faceDirection, a, b);
        }
        return null;
    }
}
