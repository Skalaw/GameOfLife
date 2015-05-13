package com.skala.gameoflife.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skala.gameoflife.R;
import com.skala.gameoflife.utils.FileUtils;

/**
 * @author Skala
 */
public class SettingsFragment extends PreferenceFragment {
    private final String BOARD_NOT_LOAD = "";

    private SettingsDrawerListener mSettingsListener;

    private boolean mIsRefreshIntervalUpdate = false;
    private boolean mIsBoardRowUpdate = false;
    private boolean mIsBoardColumnUpdate = false;
    private String mLoadBoardName = BOARD_NOT_LOAD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String key;

        key = getResources().getString(R.string.interval_update_key);
        findPreference(key).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mIsRefreshIntervalUpdate = true;
                return true;
            }
        });

        key = getResources().getString(R.string.board_row_key);
        findPreference(key).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mIsBoardRowUpdate = true;
                return true;
            }
        });

        key = getResources().getString(R.string.board_column_key);
        findPreference(key).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mIsBoardColumnUpdate = true;
                return true;
            }
        });

        key = getResources().getString(R.string.board_list_key);
        findPreference(key).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                createListBoardsPreferenceDialog();
                return false;

            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void createListBoardsPreferenceDialog() {
        Dialog dialog;
        final String[] fileNames = FileUtils.getListBoardFromAssets(getActivity());
        int fileCount = fileNames.length;
        String[] displayNames = new String[fileCount];
        for (int i = 0; i < fileCount; i++) {
            if (fileNames[i].endsWith(".json")) {
                displayNames[i] = fileNames[i].substring(0, fileNames[i].length() - 5);
            }
        }

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle(R.string.board_list_dialog_title);
        b.setItems(displayNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                mLoadBoardName = fileNames[position];
                mSettingsListener.closeDrawer();
            }
        });
        b.setNegativeButton("Cancel", null);

        dialog = b.create();
        dialog.show();
    }

    public void updateSettings() {
        if (mIsRefreshIntervalUpdate) {
            mSettingsListener.updateTimeInterval();
            mIsRefreshIntervalUpdate = false;
        }

        // first load custom board if is choice, if not then check is update size boards
        if (!mLoadBoardName.equals(BOARD_NOT_LOAD)) {
            mSettingsListener.loadBoard(mLoadBoardName);
            mLoadBoardName = BOARD_NOT_LOAD; // set to not choice for next close drawer
        } else {
            if (mIsBoardRowUpdate || mIsBoardColumnUpdate) {
                mSettingsListener.updateSizeBoard();
                mIsBoardRowUpdate = false;
                mIsBoardColumnUpdate = false;
            }
        }
    }

    public void setSettingsListener(SettingsDrawerListener settingsListener) {
        mSettingsListener = settingsListener;
    }
}
