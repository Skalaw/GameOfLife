package com.skala.gameoflife;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Skala
 */

public class MainActivity extends Activity {
    private GameView mGameView;
    private DrawerLayout mDrawerLayout;
    private ViewGroup mLeftDrawer;

    private SurfaceListener mSurfaceListener = new SurfaceListener() {
        @Override
        public void openDrawer() {
            mDrawerLayout.openDrawer(mLeftDrawer);
        }
    };

    private SettingsDrawerListener mSettingsDrawerListener = new SettingsDrawerListener() {
        @Override
        public void refreshSurface() {
            mGameView.loadSettings();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        setContentView(R.layout.activity_main);

        mGameView = new GameView(this);
        mGameView.init(displaymetrics.widthPixels, displaymetrics.heightPixels);
        mGameView.setSurfaceListener(mSurfaceListener);
        ((ViewGroup) findViewById(R.id.content)).addView(mGameView);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawer = (ViewGroup) findViewById(R.id.leftDrawer);

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mSettingsDrawerListener.refreshSurface();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
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
