package com.example.android.hedgehogorpenguin;

import android.graphics.Paint;

/**
 * Created by Роман on 28.01.2018.
 */

public class Animal {

    private int xPos;
    private int yPos;

    private int speedX;
    private int speedY;

    private int width;
    protected int height;

    private int circleRadius;
    protected Paint circlePaint;

    Animal(int width, int height) {
        this.circleRadius = 105;
        this.xPos = (int) (50 + (Math.random() * 500));
        this.height = height;
        this.width = width;
        this.yPos = this.circleRadius;
        int speed = 2 + (int) (Math.random() * 6);
        this.speedX = speed;
        this.speedY = speed;
    }

    int getxPos() {
        return this.xPos;
    }

    int getyPos() {
        return this.yPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    void updatePhysics() {
        this.xPos += this.speedX;
        this.yPos += this.speedY;

        if (this.yPos < 0 || this.yPos > height) {
            this.speedY *= -1;
        }

        if (this.xPos < 0 || this.xPos + circleRadius > width) {
            this.speedX *= -1;
        }
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
