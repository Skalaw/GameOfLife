package com.skala.gameoflife.settings;

/**
 * @author Skala
 */
public interface SettingsDrawerListener {
    void updateSizeBoard();
    void updateTimeInterval();
    void closeDrawer();
    void loadBoard(int loadBoardNumber);
}
