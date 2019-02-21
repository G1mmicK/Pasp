package com.github.g1mmick.pasp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.github.g1mmick.pasp.R;
import com.github.g1mmick.pasp.model.Block;
import com.github.g1mmick.pasp.model.Character;
import com.github.g1mmick.pasp.utils.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.List;

public class GameEngine extends SurfaceView implements Runnable {

    private static final long MILLIS_PER_SECOND = 1000;
    private static final long FPS = 60;
    private static final int NUM_BLOCKS_HIGH = 5;
    private static final int NUM_BLOCKS_WIDE = 9;

    private Thread thread = null;

    private Context context;

    private int screenX;
    private int screenY;

    private int blockSize;

    private long nextFrameTime;
    private volatile boolean isPlaying;

    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    Bitmap bg;
    Rect bgCut;
    Rect bgRect;

    private Character player;
    private List<Block> blocks = new ArrayList<>();

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

        bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
        int bgCutWidth = Math.min(bg.getWidth(), bg.getHeight() * screenX / screenY);
        int bgCutHeight = Math.min(bg.getHeight(), bg.getWidth() * screenY / screenX);
        int bgCutX = (bg.getWidth() - bgCutWidth) / 2;
        int bgCutY = (bg.getHeight() - bgCutHeight) / 2;
        bgCut = new Rect(bgCutX, bgCutY, bgCutWidth + bgCutX, bgCutHeight + bgCutY);
        bgRect = new Rect(0, 0, screenX, screenY);

        player = new Character(BitmapFactory.decodeResource(context.getResources(), R.drawable.player), blockSize);
        blocks.add(new Block(BitmapFactory.decodeResource(context.getResources(), R.drawable.box_block), blockSize, 0 * blockSize, 3 * blockSize));
        blocks.add(new Block(BitmapFactory.decodeResource(context.getResources(), R.drawable.box_block), blockSize, 7 * blockSize, 2 * blockSize));
        blocks.add(new Block(BitmapFactory.decodeResource(context.getResources(), R.drawable.box_block), blockSize, 4 * blockSize, 0 * blockSize));
        blocks.add(new Block(BitmapFactory.decodeResource(context.getResources(), R.drawable.box_block), blockSize, 5 * blockSize, 3 * blockSize));
        blocks.add(new Block(BitmapFactory.decodeResource(context.getResources(), R.drawable.box_block), blockSize, 3 * blockSize, 4 * blockSize));
        blocks.add(new Block(BitmapFactory.decodeResource(context.getResources(), R.drawable.box_block), blockSize, 2 * blockSize, 1 * blockSize));

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void draw() {
        // Get a lock on the canvas
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            // Fill the screen
            //canvas.drawColor(Color.argb(255, 30, 120, 190));
            canvas.drawBitmap(bg, null, bgRect, null);

            for (Block block : blocks) {
                canvas.drawBitmap(block.getBitmap(), null, block.getHitbox(), null);
            }

            //canvas.drawBitmap(player.getBitmap(), player.x, player.y, paint);
            canvas.drawBitmap(player.getBitmap(), null, player.getHitbox(), null);
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
        player.update(FPS);
        checkCollision();
    }

    private void checkCollision() {
        switch (player.getHeading()) {
            case UP:
                if (player.getHitbox().top <= 0) {
                    player.setY(0);
                    player.stop();
                }
                break;
            case RIGHT:
                if (player.getHitbox().right >= screenX) {
                    player.setX(screenX - player.getHitbox().width());
                    player.stop();
                }
                break;
            case DOWN:
                if (player.getHitbox().bottom >= screenY) {
                    player.setY(screenY - player.getHitbox().height());
                    player.stop();
                }
                break;
            case LEFT:
                if (player.getHitbox().left <= 0) {
                    player.setX(0);
                    player.stop();
                }
                break;
        }

        for (Block block : blocks) {
            if (RectF.intersects(player.getHitbox(), block.getHitbox())) {
                switch (player.getHeading()) {
                    case UP:
                        player.setY(block.getHitbox().bottom);
                        break;
                    case RIGHT:
                        player.setX(block.getHitbox().left - player.getHitbox().width());
                        break;
                    case DOWN:
                        player.setY(block.getHitbox().top - player.getHitbox().height());
                        break;
                    case LEFT:
                        player.setX(block.getHitbox().right);
                        break;
                }
                player.stop();
            }
        }
    }

    private void newGame() {
        player.setX(0);
        player.setY(0);

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
