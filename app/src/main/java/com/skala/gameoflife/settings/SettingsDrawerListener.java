package com.skala.gameoflife.settings;

/**
 * @author Skala
 */
public interface SettingsDrawerListener {
    void updateSizeBoard();
    void updateTimeInterval();
    void closeDrawer();
    void loadBoardFromAssets(String boardName);
    void loadBoardFromExternal(String boardName);
    void saveBoardToExternal(String fileName);
}
