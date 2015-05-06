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

import com.skala.gameoflife.game.GameView;
import com.skala.gameoflife.game.SurfaceListener;
import com.skala.gameoflife.settings.SettingsDrawerListener;
import com.skala.gameoflife.settings.SettingsFragment;

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
        public void updateSizeBoard() {
            int row = getRowsNumber();
            int column = getColumnsNumber();

            mGameView.updateBoard(row, column);
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
        String defaultValue = String.valueOf(getResources().getInteger(R.integer.interval_update_default));
        String value = sharedPreferences.getString(key, defaultValue);

        int updateTime;
        if (value.isEmpty()) {
            updateTime = 0;
        } else {
            updateTime = Integer.parseInt(value);
        }
        return updateTime;
    }

    public int getRowsNumber() {
        String key = getString(R.string.board_row_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String defaultValue = String.valueOf(getResources().getInteger(R.integer.board_row_default));
        String value = sharedPreferences.getString(key, defaultValue);

        int rowNumber;
        if (value.isEmpty()) {
            rowNumber = 3; // minimum value
        } else {
            rowNumber = Integer.parseInt(value);
            if (rowNumber < 3) {
                rowNumber = 3;
            }
        }

        return rowNumber;
    }

    public int getColumnsNumber() {
        String key = getString(R.string.board_column_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String defaultValue = String.valueOf(getResources().getInteger(R.integer.board_column_default));
        String value = sharedPreferences.getString(key, defaultValue);

        int columnNumber;
        if (value.isEmpty()) {
            columnNumber = 3; // minimum value
        } else {
            columnNumber = Integer.parseInt(value);
            if (columnNumber < 3) {
                columnNumber = 3;
            }
        }

        return columnNumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // load settings drawer
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mSettingsFragment = new SettingsFragment();
        mSettingsFragment.setSettingsListener(mSettingsDrawerListener);
        fragmentTransaction.add(R.id.leftDrawer, mSettingsFragment);
        fragmentTransaction.commit();

        // drawer
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

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        // create game
        mGameView = new GameView(this);
        mGameView.init(displaymetrics.widthPixels, displaymetrics.heightPixels, getRowsNumber(), getColumnsNumber());
        mGameView.setSurfaceListener(mSurfaceListener);
        mSettingsDrawerListener.updateTimeInterval();  // TODO: poprawic
        ((ViewGroup) findViewById(R.id.content)).addView(mGameView);
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
