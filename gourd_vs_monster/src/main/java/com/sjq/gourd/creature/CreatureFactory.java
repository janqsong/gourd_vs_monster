package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

public class CreatureFactory {
    private final String camp;
    private final int faceDirection;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final int minId;
    private final int maxId;
    private int currentId;
    //确保size至少有2*(maxId-minId+1)
    private ArrayList<ImageView> imageViews;

    public CreatureFactory(String camp, int faceDirection, DataInputStream in, DataOutputStream out, ArrayList<ImageView> imageViews) throws Exception {
        this.camp = camp;
        this.faceDirection = faceDirection;
        this.in = in;
        this.out = out;
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
                return new FirstGourd(in, out, faceDirection, a, b);
            if (currentId == CreatureId.SECOND_GOURD_ID)
                return new SecondGourd(in, out, faceDirection, a, b);
            if (currentId == CreatureId.THIRD_GOURD_ID)
                return new ThirdGourd(in, out, faceDirection, a, b);
            if (currentId == CreatureId.FOURTH_GOURD_ID)
                return new FourthGourd(in, out, faceDirection, a, b);
            if (currentId == CreatureId.FIFTH_GOURD_ID)
                return new FifthGourd(in, out, faceDirection, a, b);
            if (currentId == CreatureId.SIXTH_GOURD_ID)
                return new SixthGourd(in, out, faceDirection, a, b);
            if (currentId == CreatureId.SEVENTH_GOURD_ID)
                return new SeventhGourd(in, out, faceDirection, a, b);
            if (currentId == CreatureId.PANGOLIN_ID)
                return new Pangolin(in, out, faceDirection, a, b);
            if (currentId == CreatureId.GRANDPA_ID)
                return new Grandpa(in, out, faceDirection, a, b);
        } else {
            if (currentId == CreatureId.SNAKE_MONSTER_ID)
                return new SnakeMonster(in, out, faceDirection, a, b);
            if (currentId == CreatureId.SCORPION_MONSTER_ID)
                return new ScorpionMonster(in, out, faceDirection, a, b);
            if (currentId == CreatureId.MONSTER1_ID)
                return new CentipedeMonster(in, out, faceDirection, a, b);
            if (currentId == CreatureId.MONSTER2_ID)
                return new BatMonster(in, out, faceDirection, a, b);
            if (currentId == CreatureId.MONSTER3_ID)
                return new CrocodileMonster(in, out, faceDirection, a, b);
            if (currentId == CreatureId.MONSTER4_ID)
                return new ToadMonster(in, out, faceDirection, a, b);
        }
        return null;
    }
}
