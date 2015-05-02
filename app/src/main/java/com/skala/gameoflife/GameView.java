package com.skala.gameoflife;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.skala.gameoflife.surfaceobject.Cell;
import com.skala.gameoflife.surfaceobject.TextObj;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Skala
 */

public class GameView extends SurfaceView implements Runnable, View.OnTouchListener {
    private final static int ROW_CELL = 10;
    private final static int COLUMN_CELL = 10;

    private Thread mThreadGame;
    private boolean mRunningGame = false;
    private SurfaceHolder mHolder;
    private Paint mPaintTime = new Paint();
    private Paint mPaintFPS = new Paint();

    private TimerHelper mTimerHelper;

    private int mWidthScreen;
    private int mHeightScreen;

    private TextObj mTextReset;
    private TextObj mTextRandom;
    private TextObj mTextPlay;
    private ArrayList<Cell> mCellList;

    private boolean mPlayGame = false;

    public GameView(Context context) {
        super(context);
    }

    public void init(int widthScreen, int heightScreen) {
        mWidthScreen = widthScreen;
        mHeightScreen = heightScreen;

        mHolder = getHolder();
        mPaintTime.setTextAlign(Paint.Align.CENTER);
        mPaintTime.setTextSize(42);

        mPaintFPS.setTextAlign(Paint.Align.LEFT);
        mPaintFPS.setTextSize(42);

        int sizeCell = (int) ((float) widthScreen * 0.8f / ROW_CELL);
        mCellList = createBoard(sizeCell);
        setNeighbour();

        int halfScreen = mWidthScreen / 2;

        mTextRandom = new TextObj(halfScreen - 90, 780, halfScreen + 90, 840);
        mTextRandom.setText("RANDOM");

        mTextReset = new TextObj(halfScreen - 90, 870, halfScreen + 90, 930);
        mTextReset.setText("RESET");

        mTextPlay = new TextObj(halfScreen - 90, 960, halfScreen + 90, 1020);
        mTextPlay.setText("PLAY");

        setOnTouchListener(this);

        mTimerHelper = new TimerHelper();
    }

    private ArrayList<Cell> createBoard(int sizeCell) {
        ArrayList<Cell> arrayList = new ArrayList<>();
        int offsetX = (int) (mWidthScreen * 0.1f);
        int offsetY = (int) (mWidthScreen * 0.1f);

        // distance between cells
        int spaceX, spaceY = 1;

        Cell cell;
        for (int i = 0; i < COLUMN_CELL; i++) {
            int top = i * sizeCell + offsetY + spaceY;
            int bottom = top + sizeCell;

            spaceX = 1;
            spaceY++;
            for (int j = 0; j < ROW_CELL; j++) {
                int left = j * sizeCell + offsetX + spaceX;
                int right = left + sizeCell;
                cell = new Cell(left, top, right, bottom);
                arrayList.add(cell);

                spaceX++;
            }
        }

        return arrayList;
    }

    private void setNeighbour() {
        Cell cell;

        int size = mCellList.size();
        for (int i = 0; i < size; i++) {
            cell = mCellList.get(i);

            int right = getRightCell(i);
            cell.setRightNeight(mCellList.get(right));

            int left = getLeftCell(i);
            cell.setLeftNeigh(mCellList.get(left));

            int top = getTopCell(i);
            cell.setTopNeigh(mCellList.get(top));

            int bottom = getBottomCell(i);
            cell.setBottomNeight(mCellList.get(bottom));

            int rt = getTopCell(right);
            cell.setRTNeigh(mCellList.get(rt));

            int rb = getBottomCell(right);
            cell.setRBNeigh(mCellList.get(rb));

            int lt = getTopCell(left);
            cell.setLTNeigh(mCellList.get(lt));

            int lb = getBottomCell(left);
            cell.setLBNeigh(mCellList.get(lb));

            //Log.d("GameView", "Aktualny: " + i + " left: " + left + " right: " + right + " top: " + top + " bottom: " + bottom + " lt: " + lt + " lb: " + lb + " rt: " + rt + " rb: " + rb);
        }
    }

    private int getRightCell(int number) {
        int integerDivision = number / ROW_CELL;
        int modulo = (number + 1) % ROW_CELL;
        return integerDivision * ROW_CELL + modulo;
    }

    private int getLeftCell(int number) {
        int integerDivision = number / ROW_CELL;
        int modulo = number % ROW_CELL;
        if (modulo == 0) {
            modulo = ROW_CELL;
        }
        modulo--;

        return integerDivision * ROW_CELL + modulo;
    }

    private int getTopCell(int number) {
        int top = (number - ROW_CELL);
        if (top < 0) {
            top += COLUMN_CELL * ROW_CELL;
        }
        return top;
    }

    private int getBottomCell(int number) {
        int bottom = (number + ROW_CELL) % (COLUMN_CELL * ROW_CELL);
        return bottom;
    }

    public void onResume() {
        mTimerHelper.onResume();

        mRunningGame = true;
        mThreadGame = new Thread(this);
        mThreadGame.start();
    }

    public void onPause() {
        mRunningGame = false;
        while (true) {
            try {
                mThreadGame.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mThreadGame = null;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        while (mRunningGame) {
            if (!mHolder.getSurface().isValid()) {
                continue;
            }

            mTimerHelper.nextFrame();

            if (mPlayGame && mTimerHelper.isNextFrameAvailable()) {
                game();
                mTimerHelper.setStateTime();
            }

            Canvas c = mHolder.lockCanvas();
            onDraw(c);
            mHolder.unlockCanvasAndPost(c);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFFFBB33);

        int size = mCellList.size();
        for (int i = 0; i < size; i++) {
            mCellList.get(i).onDraw(canvas);
        }

        mTextRandom.onDraw(canvas);
        mTextReset.onDraw(canvas);
        mTextPlay.onDraw(canvas);

        canvas.drawText(String.valueOf(mTimerHelper.getTimeInSec()), 380, 32, mPaintTime);
        canvas.drawText("FPS: " + mTimerHelper.getFPSParse(), 580, 32, mPaintFPS);
    }

    @Override
    public boolean onTouch(View v, MotionEvent me) {
        int x = (int) me.getX();
        int y = (int) me.getY();

        switch (me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //startTouch();
                break;

            case MotionEvent.ACTION_UP:
                int size = mCellList.size();
                for (int i = 0; i < size; i++) {
                    Cell cell = mCellList.get(i);
                    if (cell.isClicked(x, y)) {
                        cell.toggleAlive();
                        break;
                    }
                }

                if (mTextReset.isClicked(x, y)) {
                    resetBoard();
                } else if (mTextRandom.isClicked(x, y)) {
                    randomBoard();
                } else if (mTextPlay.isClicked(x, y)) {
                    playGame();
                }

                //endTouchPut();
                break;

            case MotionEvent.ACTION_MOVE:
                //moveTouch();
                break;
        }
        return true;
    }

    private void resetBoard() {
        int size = mCellList.size();
        for (int i = 0; i < size; i++) {
            mCellList.get(i).setIsAlive(false);
        }
    }

    private void randomBoard() {
        Random random = new Random();
        int size = mCellList.size();
        for (int i = 0; i < size; i++) {
            mCellList.get(i).setIsAlive(random.nextBoolean());
        }
    }

    private void playGame() {
        mPlayGame = !mPlayGame;
        if (mPlayGame) {
            mTextPlay.setText("STOP");
        } else {
            mTextPlay.setText("PLAY");
        }
    }

    private void game() {
        int count = mCellList.size();
        for (int i = 0; i < count; i++) {
            Cell cell = mCellList.get(i);
            int countAliveNeight = cell.getAliveNeight();
            boolean isCellAlive;
            if (cell.isAlive()) {
                isCellAlive = countAliveNeight == 2 || countAliveNeight == 3;
            } else {
                isCellAlive = countAliveNeight == 3;
            }
            cell.setIsAliveNext(isCellAlive);

        }

        for (int i = 0; i < count; i++) {
            mCellList.get(i).saveState();
        }
    }

}
