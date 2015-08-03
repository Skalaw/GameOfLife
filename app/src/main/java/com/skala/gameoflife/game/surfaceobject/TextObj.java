package com.skala.gameoflife.game.surfaceobject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @author Skala
 */

public class TextObj implements SurfaceObject {
    private static final int PADDING_TEXT_TOP = 16;
    private static Paint mPaintText = new Paint();
    private static Paint mPaintRect = new Paint();

    private Rect mRect;
    private String mText;

    static {
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setTextSize(42);

        mPaintRect.setColor(0xFF33B5E5);
    }

    public TextObj(int left, int top, int right, int bottom) {
        mRect = new Rect(left, top, right, bottom);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(mRect, mPaintRect);
        canvas.drawText(mText, mRect.centerX(), mRect.centerY() + PADDING_TEXT_TOP, mPaintText);
    }

    @Override
    public boolean isClicked(int x, int y) {
        return mRect.contains(x, y);
    }

    public void setText(String text) {
        mText = text;
    }
}
