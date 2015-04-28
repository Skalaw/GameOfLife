package com.skala.gameoflife.surfaceobject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @author Skala
 */

public class Cell implements SurfaceObject {
    private Rect mRect;
    private boolean mIsAlive = false;
    private boolean mIsAliveNext = false;

    private Cell mLeftNeigh;
    private Cell mTopNeigh;
    private Cell mRightNeight;
    private Cell mBottomNeight;

    private Cell mLTNeigh; // left top
    private Cell mLBNeigh; // left bottom
    private Cell mRTNeigh; // right top
    private Cell mRBNeigh; // right bottom

    private static Paint mPaintCellAlive = new Paint();
    private static Paint mPaintCellDead = new Paint();

    static {
        mPaintCellAlive.setColor(0xFF33B5E5);
        mPaintCellDead.setColor(0xFFFF4444);
    }

    public Cell(int left, int top, int right, int bottom) {
        mRect = new Rect(left, top, right, bottom);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mIsAlive) {
            canvas.drawRect(mRect, mPaintCellAlive);
        } else {
            canvas.drawRect(mRect, mPaintCellDead);
        }
    }

    @Override
    public boolean isClicked(int x, int y) {
        return mRect.contains(x, y);
    }

    public boolean isAlive() {
        return mIsAlive;
    }

    public void setIsAlive(boolean isAlive) {
        mIsAlive = isAlive;
    }

    public void toggleAlive() {
        mIsAlive = !mIsAlive;
    }

    public void setLeftNeigh(Cell leftNeigh) {
        mLeftNeigh = leftNeigh;
    }

    public void setTopNeigh(Cell topNeigh) {
        mTopNeigh = topNeigh;
    }

    public void setRightNeight(Cell rightNeight) {
        mRightNeight = rightNeight;
    }

    public void setBottomNeight(Cell bottomNeight) {
        mBottomNeight = bottomNeight;
    }

    public void setLTNeigh(Cell LTNeigh) {
        mLTNeigh = LTNeigh;
    }

    public void setLBNeigh(Cell LBNeigh) {
        mLBNeigh = LBNeigh;
    }

    public void setRTNeigh(Cell RTNeigh) {
        mRTNeigh = RTNeigh;
    }

    public void setRBNeigh(Cell RBNeigh) {
        mRBNeigh = RBNeigh;
    }

    public int getAliveNeight() {
        int countNeighAlive = 0;

        if (mLeftNeigh.isAlive()) {
            countNeighAlive++;
        }
        if (mRightNeight.isAlive()) {
            countNeighAlive++;
        }
        if (mBottomNeight.isAlive()) {
            countNeighAlive++;
        }
        if (mTopNeigh.isAlive()) {
            countNeighAlive++;
        }

        if (mLTNeigh.isAlive()) {
            countNeighAlive++;
        }
        if (mLBNeigh.isAlive()) {
            countNeighAlive++;
        }
        if (mRTNeigh.isAlive()) {
            countNeighAlive++;
        }
        if (mRBNeigh.isAlive()) {
            countNeighAlive++;
        }

        return countNeighAlive;
    }

    public void setIsAliveNext(boolean isAliveNext) {
        mIsAliveNext = isAliveNext;
    }

    public void saveState() {
        mIsAlive = mIsAliveNext;
    }
}
