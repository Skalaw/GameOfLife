package com.skala.gameoflife.game.surfaceobject;

import android.graphics.Canvas;

/**
 * @author Skala
 */

public interface SurfaceObject {
    void onDraw(Canvas canvas);

    boolean isClicked(int x, int y);
}
