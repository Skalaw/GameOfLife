package com.skala.gameoflife;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Skala
 */
public class SettingsFragment extends PreferenceFragment {
    private boolean mIsRefreshIntervalUpdate = false;
    private SettingsDrawerListener mSettingsListener;

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

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void updateSettings() {
        if (mIsRefreshIntervalUpdate) {
            mSettingsListener.updateTimeInterval();
            mIsRefreshIntervalUpdate = false;
        }
    }

    public void setSettingsListener(SettingsDrawerListener settingsListener) {
        mSettingsListener = settingsListener;
    }
}
