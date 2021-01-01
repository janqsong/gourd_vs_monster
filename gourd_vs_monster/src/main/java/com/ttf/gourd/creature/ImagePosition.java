package com.ttf.gourd.creature;

import com.ttf.gourd.constant.Constant;

public class ImagePosition {
    private double layoutX;
    private double layoutY;

    public ImagePosition(double layoutX, double layoutY) {
        this.layoutX = layoutX;
        this.layoutY = layoutY;
    }

    public double getLayoutX() {
        return layoutX;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
    }

    public double getLayoutY() {
        return layoutY;
    }

    public double getDistance(ImagePosition imagePosition) {
        double x = layoutX - imagePosition.layoutX;
        double y = layoutY - imagePosition.layoutY;
        return Math.sqrt(x * x + y * y);
    }

    public int getRelativePosClose(ImagePosition imagePosition) {
        double deltaX = layoutX - imagePosition.layoutX;
        double deltaY = layoutY - imagePosition.layoutY;
        double x = Math.abs(deltaX);
        double y = Math.abs(deltaY);
        if (x < y) {
            if (deltaX < 0) return Constant.Direction.RIGHT;
            else return Constant.Direction.LEFT;
        } else {
            if (deltaY < 0) return Constant.Direction.DOWN;
            else return Constant.Direction.UP;
        }
    }

    public int getRelativePosFar(ImagePosition imagePosition) {
        double deltaX = layoutX - imagePosition.layoutX;
        double deltaY = layoutY - imagePosition.layoutY;
        double x = Math.abs(deltaX);
        double y = Math.abs(deltaY);
        if (x < y) {
            if (deltaY < 0) return Constant.Direction.DOWN;
            else return Constant.Direction.UP;
        } else {
            if (deltaX < 0) return Constant.Direction.RIGHT;
            else return Constant.Direction.LEFT;
        }
    }
}

