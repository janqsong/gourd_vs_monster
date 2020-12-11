package com.sjq.gourd.client;

import java.util.ArrayList;

import com.sjq.gourd.stage.SceneController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MonsterCamp {
    private SceneController sceneController;
    ArrayList<ImageView> monsterImageList = new ArrayList<>();

    public MonsterCamp(SceneController sceneController) {
        ImageView itemImageView1 = new ImageView();
        ImageView itemImageView2 = new ImageView();
        ImageView itemImageView3 = new ImageView();
        ImageView itemImageView4 = new ImageView();
        ImageView itemImageView5 = new ImageView();
        ImageView itemImageView6 = new ImageView();
        ImageView itemImageView7 = new ImageView();
        ImageView itemImageView8 = new ImageView();
        ImageView itemImageView9 = new ImageView();
        Image itemImage1 = new Image("/蜈蚣精.png");
        Image itemImage2 = new Image("/蜈蚣精.png");
        Image itemImage3 = new Image("/蜈蚣精.png");
        Image itemImage4 = new Image("/蜈蚣精.png");
        Image itemImage5 = new Image("/蜈蚣精.png");
        Image itemImage6 = new Image("/蜈蚣精.png");
        Image itemImage7 = new Image("/蜈蚣精.png");
        Image itemImage8 = new Image("/蜈蚣精.png");
        Image itemImage9 = new Image("/蜈蚣精.png");
        itemImageView1.setImage(itemImage1);
        itemImageView2.setImage(itemImage2);
        itemImageView3.setImage(itemImage3);
        itemImageView4.setImage(itemImage4);
        itemImageView5.setImage(itemImage5);
        itemImageView6.setImage(itemImage6);
        itemImageView7.setImage(itemImage7);
        itemImageView8.setImage(itemImage8);
        itemImageView9.setImage(itemImage9);
        monsterImageList.add(itemImageView1);
        monsterImageList.add(itemImageView2);
        monsterImageList.add(itemImageView3);
        monsterImageList.add(itemImageView4);
        monsterImageList.add(itemImageView5);
        monsterImageList.add(itemImageView6);
        monsterImageList.add(itemImageView7);
        monsterImageList.add(itemImageView8);
        monsterImageList.add(itemImageView9);
    }

    public void initGame() {

        startGame();
    }

    public void startGame() {

    }
}
