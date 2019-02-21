package com.github.g1mmick.pasp.model;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class Block {

    private RectF hitbox;
    private Bitmap bitmap;

    public Block(Bitmap bitmap, int size, int x, int y) {
        this.bitmap = bitmap;
        hitbox = new RectF(x, y, x + size, y + size);
    }

    public RectF getHitbox() {
        return hitbox;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
