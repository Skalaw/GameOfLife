package com.skala.gameoflife;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * @author Skala
 */

public class MainActivity extends Activity {
    private GameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        mGameView = new GameView(this);
        mGameView.init(displaymetrics.widthPixels, displaymetrics.heightPixels);
        setContentView(mGameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGameView.onResume();
    }

    @Override
    protected void onPause() {
        mGameView.onPause();
        super.onPause();
    }
}
