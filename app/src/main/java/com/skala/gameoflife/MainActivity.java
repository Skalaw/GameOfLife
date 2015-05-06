package com.skala.gameoflife;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    private SettingsFragment mSettingsFragment;

    private SurfaceListener mSurfaceListener = new SurfaceListener() {
        @Override
        public void openDrawer() {
            mDrawerLayout.openDrawer(mLeftDrawer);
        }
    };

    private SettingsDrawerListener mSettingsDrawerListener = new SettingsDrawerListener() {
        @Override
        public void refreshSurface() {
            int row = 10; // TODO
            int column = 10;

            mGameView.updateSettings(row, column);
        }

        @Override
        public void updateTimeInterval() {
            int updateTime = getIntervalUpdateValue();

            mGameView.setUpdateInterval(updateTime);
        }
    };

    public int getIntervalUpdateValue() {
        String key = getString(R.string.interval_update_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String defaultValue = getString(R.string.interval_update_default);
        String value = sharedPreferences.getString(key, defaultValue);

        int updateTime;
        if (value.isEmpty()) {
            updateTime = 0;
        } else {
            updateTime = Integer.parseInt(value);
        }
        return updateTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mSettingsFragment = new SettingsFragment();
        mSettingsFragment.setSettingsListener(mSettingsDrawerListener);
        fragmentTransaction.add(R.id.leftDrawer, mSettingsFragment);
        fragmentTransaction.commit();


        mGameView = new GameView(this);
        mGameView.init(displaymetrics.widthPixels, displaymetrics.heightPixels);
        mGameView.setSurfaceListener(mSurfaceListener);
        mSettingsDrawerListener.updateTimeInterval();  // TODO: poprawic
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
                mSettingsFragment.updateSettings();
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
