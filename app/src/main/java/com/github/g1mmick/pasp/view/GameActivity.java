package com.github.g1mmick.pasp.view;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;

import com.github.g1mmick.pasp.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    ViewGroup gameContainerLayout;

    GameEngine gameEngine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initViews();
    }

    private void initViews() {
        gameContainerLayout = findViewById(R.id.game_container_layout);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        gameEngine = new GameEngine(this, size);
        gameContainerLayout.addView(gameEngine);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameEngine.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameEngine.pause();
    }
}
