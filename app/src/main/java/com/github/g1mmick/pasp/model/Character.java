package com.github.g1mmick.pasp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.github.g1mmick.pasp.R;

public class Character {

    public enum Heading {NONE, UP, RIGHT, DOWN, LEFT}

    private Heading heading = Heading.NONE;
    private Heading nextHeading = Heading.NONE;
    public int x, y;
    private double speed = 16;
    private int size;
    private Rect hitbox;
    private Bitmap bitmap;

    public Character(Context context, int size) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.character);
        hitbox = new Rect(x, y, x + size, y + size);
        this.size = size;
    }

    public void update() {
        /*switch (heading) {
            case UP:
                y -= speed * size / FPS;
                if (player.y <= 0) {
                    player.y = 0;
                    player.stop();
                }
                break;
            case RIGHT:
                player.x += player.getSpeed() * blockSize / FPS;
                if (player.x >= screenX - blockSize) {
                    player.x = screenX - blockSize;
                    player.stop();
                }
                break;
            case DOWN:
                player.y += player.getSpeed() * blockSize / FPS;
                if (player.y >= screenY - blockSize) {
                    player.y = screenY - blockSize;
                    player.stop();
                }
                break;
            case LEFT:
                player.x -= player.getSpeed() * blockSize / FPS;
                if (player.x <= 0) {
                    player.x = 0;
                    player.stop();
                }
                break;
        }*/
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

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public Heading getNextHeading() {
        return nextHeading;
    }

    public void setNextHeading(Heading nextHeading) {
        this.nextHeading = nextHeading;
    }

    public double getSpeed() {
        return speed;
    }

    public Rect getHitbox() {
        return hitbox;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
