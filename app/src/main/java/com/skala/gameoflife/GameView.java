package com.skala.gameoflife;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.skala.gameoflife.surfaceobject.Cell;
import com.skala.gameoflife.surfaceobject.TextObj;

/**
 * @author Skala
 */

public class GameView extends SurfaceView implements Runnable, View.OnTouchListener {
    private final static long INTERVAL = 200;
    private final static int ROW_CELL = 10;
    private final static int COLUMN_CELL = 10;

    private Thread mThreadGame;
    private boolean mRunningGame = false;
    private SurfaceHolder mHolder;
    private Paint mPaintTime = new Paint();
    private Paint mPaintFPS = new Paint();

    private int mWidthScreen;
    private int mHeightScreen;

    private long mTimeStart = 0;
    private String mFPS = "";
    private DecimalFormat mDF = new DecimalFormat("#.#");
    private long mTimeSec = 0;
    private long mTimeMilis;
    private long mPreviousStateTimeMilis = 0;

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

        // kod szybciej się wykonuje alemniej czytelny
        /*for (int i = 0; i < COLUMN_CELL; i++) {
            int columnCount = i * ROW_CELL;
            for (int j = 0; j < ROW_CELL; j++) {
                cell = mCellList.get(columnCount + j);

                // right
                int right = columnCount + (j + 1) % ROW_CELL;

                // left
                int pos = (j - 1);
                if (pos < 0) {
                    pos += ROW_CELL;
                }
                int left = columnCount + pos;

                // top
                int top = (columnCount + j - ROW_CELL);
                if (top < 0) {
                    top += COLUMN_CELL * ROW_CELL;
                }

                // bottom
                int bottom = (columnCount + j + ROW_CELL) % (COLUMN_CELL * ROW_CELL);

                // right top
                int mRTNeigh = right - ROW_CELL;
                if (mRTNeigh < 0) {
                    mRTNeigh += COLUMN_CELL * ROW_CELL;
                }

                // right bottom
                int mRBNeigh = (right + ROW_CELL) % (COLUMN_CELL * ROW_CELL);

                // left top
                int mLTNeigh = left - ROW_CELL;
                if (mLTNeigh < 0) {
                    mLTNeigh += COLUMN_CELL * ROW_CELL;
                }

                // left bottom
                int mLBNeigh = (left + ROW_CELL) % (COLUMN_CELL * ROW_CELL);


                cell.setRightNeight(mCellList.get(right));
                cell.setLeftNeigh(mCellList.get(left));
                cell.setTopNeigh(mCellList.get(top));
                cell.setBottomNeight(mCellList.get(bottom));

                cell.setRTNeigh(mCellList.get(mRTNeigh));
                cell.setRBNeigh(mCellList.get(mRBNeigh));
                cell.setLTNeigh(mCellList.get(mLTNeigh));
                cell.setLBNeigh(mCellList.get(mLBNeigh));

                //Log.d("GameView", "Aktualny: " + (i * COLUMN_CELL + j) + " left: " + left + " right: " + right + " top: " + top + " bottom: " + bottom + " lt: " + mLTNeigh + " lb: " + mLBNeigh + " rt: " + mRTNeigh + " rb: " + mRBNeigh);
            }
        }*/

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
        mTimeStart = System.currentTimeMillis();
        mRunningGame = true;

        mTimes.addLast((double) System.nanoTime());

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
            mTimeMilis = System.currentTimeMillis();
            mTimeSec = (mTimeMilis - mTimeStart) / 1000;
            mFPS = mDF.format(getFPS());

            if (mPlayGame && (mPreviousStateTimeMilis + INTERVAL < mTimeMilis)) {
                game();

                mPreviousStateTimeMilis = mTimeMilis;
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

        canvas.drawText(String.valueOf(mTimeSec), 380, 32, mPaintTime);
        canvas.drawText("FPS: " + mFPS, 580, 32, mPaintFPS);
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

    private LinkedList<Double> mTimes = new LinkedList<>();
    private final int MAX_SIZE = 100;
    private final double NANOS = 1000000000.0;

    private double getFPS() {
        double lastTime = System.nanoTime();
        double difference = (lastTime - mTimes.getFirst()) / NANOS;
        mTimes.addLast(lastTime);
        int size = mTimes.size();
        if (size > MAX_SIZE) {
            mTimes.removeFirst();
        }
        return difference > 0 ? mTimes.size() / difference : 0.0;
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
