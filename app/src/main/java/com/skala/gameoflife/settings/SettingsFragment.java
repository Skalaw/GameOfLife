package com.skala.gameoflife.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skala.gameoflife.R;

/**
 * @author Skala
 */
public class SettingsFragment extends PreferenceFragment {
    private SettingsDrawerListener mSettingsListener;

    private boolean mIsRefreshIntervalUpdate = false;
    private boolean mIsBoardRowUpdate = false;
    private boolean mIsBoardColumnUpdate = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String key;

        key = getResources().getString(R.string.interval_update_key);
        getPreferenceManager().findPreference(key).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mIsRefreshIntervalUpdate = true;
                return true;
            }
        });

        key = getResources().getString(R.string.board_row_key);
        getPreferenceManager().findPreference(key).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mIsBoardRowUpdate = true;
                return true;
            }
        });

        key = getResources().getString(R.string.board_column_key);
        getPreferenceManager().findPreference(key).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mIsBoardColumnUpdate = true;
                return true;
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void updateSettings() {
        if (mIsRefreshIntervalUpdate) {
            mSettingsListener.updateTimeInterval();
            mIsRefreshIntervalUpdate = false;
        }

        if (mIsBoardRowUpdate || mIsBoardColumnUpdate) {
            mSettingsListener.updateSizeBoard();
            mIsBoardRowUpdate = false;
            mIsBoardColumnUpdate = false;
        }

    }

    public void setSettingsListener(SettingsDrawerListener settingsListener) {
        mSettingsListener = settingsListener;
    }
}
