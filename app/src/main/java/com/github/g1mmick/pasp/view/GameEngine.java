package com.github.g1mmick.pasp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.github.g1mmick.pasp.model.Character;
import com.github.g1mmick.pasp.utils.OnSwipeTouchListener;

public class GameEngine extends SurfaceView implements Runnable {

    private static final long MILLIS_PER_SECOND = 1000;
    private static final long FPS = 60;
    private static final int NUM_BLOCKS_HIGH = 9;
    private static final int NUM_BLOCKS_WIDE = 16;

    private Thread thread = null;

    private Context context;

    private int screenX;
    private int screenY;

    private int blockSize;

    private long nextFrameTime;
    private volatile boolean isPlaying;

    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Paint paint;

    private Character player;

    public GameEngine(Context context, Point size) {
        super(context);

        this.context = context;

        blockSize = Math.min(size.x / NUM_BLOCKS_WIDE, size.y / NUM_BLOCKS_HIGH);
        if (blockSize == size.x / NUM_BLOCKS_WIDE) {
            screenX = size.x;
            screenY = blockSize * NUM_BLOCKS_HIGH;
        } else {
            screenY = size.y;
            screenX = blockSize * NUM_BLOCKS_WIDE;
        }
        setLayoutParams(new ViewGroup.LayoutParams(screenX, screenY));

        surfaceHolder = getHolder();
        paint = new Paint();

        player = new Character(context, blockSize);

        setOnTouchListener(new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeRight() {
                player.right();
            }

            @Override
            public void onSwipeLeft() {
                player.left();
            }

            @Override
            public void onSwipeUp() {
                player.up();
            }

            @Override
            public void onSwipeDown() {
                player.down();
            }
        });

        newGame();
    }

    @Override
    public void run() {
        while (isPlaying) {

            if (updateRequired()) {
                update();
                draw();
            }

        }
    }

    private void draw() {
        // Get a lock on the canvas
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            // Fill the screen with Game Code School blue
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            // Set the color of the paint to draw the player white
            paint.setColor(Color.argb(255, 255, 255, 255));

            //canvas.drawBitmap(player.getBitmap(), player.x, player.y, paint);
            canvas.drawBitmap(player.getBitmap(), null, player.getHitbox(), paint);
            /*canvas.drawRect(player.x,
                    player.y,
                    player.x + blockSize,
                    player.y + blockSize,
                    paint);*/

            // Unlock the canvas and reveal the graphics for this frame
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void update() {
        player.update();
        movePlayer();
    }

    private void movePlayer() {
        switch (player.getHeading()) {
            case UP:
                player.y -= player.getSpeed() * blockSize / FPS;
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
        }
    }

    public boolean updateRequired() {

        // Are we due to update the frame
        if (nextFrameTime <= System.currentTimeMillis()) {
            // Setup when the next update will be triggered
            nextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / FPS;

            // Return true so that the update and draw
            // functions are executed
            return true;
        }

        return false;
    }

    private void newGame() {
        player.x = 0;
        player.y = 0;

        nextFrameTime = System.currentTimeMillis();
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }
}
