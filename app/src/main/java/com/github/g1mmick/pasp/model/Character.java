package com.github.g1mmick.pasp.model;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class Character implements MovableObject {

    public enum Heading {NONE, UP, RIGHT, DOWN, LEFT}

    private Heading heading = Heading.NONE;
    private Heading nextHeading = Heading.NONE;
    private float x, y;
    private float speed = 8;
    private RectF hitbox;
    private Bitmap bitmap;
    private int size;

    public Character(Bitmap bitmap, int size) {
        this.bitmap = bitmap;
        this.size = size;
        hitbox = new RectF(x, y, x + size, y + size);
    }

    public void update(long FPS) {
        switch (heading) {
            case UP:
                y -= speed * size / FPS;
                break;
            case RIGHT:
                x += speed * size / FPS;
                break;
            case DOWN:
                y += speed * size / FPS;
                break;
            case LEFT:
                x -= speed * size / FPS;
                break;
        }
        hitbox.set(x, y, x + size, y + size);
    }

    public void stop() {
        heading = nextHeading;
        nextHeading = Heading.NONE;
    }

    public void right() {
        if (heading == Heading.NONE) {
            heading = Heading.RIGHT;
        } else {
            nextHeading = Heading.RIGHT;
        }
    }

    public void left() {
        if (heading == Heading.NONE) {
            heading = Heading.LEFT;
        } else {
            nextHeading = Heading.LEFT;
        }
    }

    public void up() {
        if (heading == Heading.NONE) {
            heading = Heading.UP;
        } else {
            nextHeading = Heading.UP;
        }
    }

    public void down() {
        if (heading == Heading.NONE) {
            heading = Heading.DOWN;
        } else {
            nextHeading = Heading.DOWN;
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        hitbox.set(x, y, x + size, y + size);
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        hitbox.set(x, y, x + size, y + size);
    }

    public Heading getHeading() {
        return heading;
    }

    public double getSpeed() {
        return speed;
    }

    public RectF getHitbox() {
        return hitbox;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
