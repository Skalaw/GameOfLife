package com.skala.gameoflife.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.skala.gameoflife.game.helpers.TimerHelper;
import com.skala.gameoflife.game.surfaceobject.GameBoard;
import com.skala.gameoflife.game.surfaceobject.TextObj;

/**
 * @author Skala
 */

public class GameView extends SurfaceView implements Runnable, View.OnTouchListener {
    private Thread mThreadGame;
    private boolean mRunningGame = false;
    private SurfaceHolder mHolder;
    private SurfaceListener mSurfaceListener;

    private Paint mPaintTime = new Paint();
    private Paint mPaintFPS = new Paint();

    private TimerHelper mTimerHelper;
    private GameBoard mGameBoard;

    private int mWidthScreen;
    private int mHeightScreen;

    private TextObj mTextReset;
    private TextObj mTextRandom;
    private TextObj mTextPlay;
    private TextObj mTextSettings;

    private boolean mPlayGame = false;
    private boolean mRequiresRender = true;

    public GameView(Context context) {
        super(context);
    }

    public void init(int widthScreen, int heightScreen, int row, int column) {
        mWidthScreen = widthScreen;
        mHeightScreen = heightScreen;

        mHolder = getHolder();
        mPaintTime.setTextAlign(Paint.Align.CENTER);
        mPaintTime.setTextSize(42);

        mPaintFPS.setTextAlign(Paint.Align.LEFT);
        mPaintFPS.setTextSize(42);

        mTimerHelper = new TimerHelper();
        updateBoard(row, column);

        int halfScreen = mWidthScreen / 2;
        final int widthButton = 100;
        int left = halfScreen - widthButton;
        int right = halfScreen + widthButton;

        mTextRandom = new TextObj(left, 740, right, 800);
        mTextRandom.setText("RANDOM");

        mTextReset = new TextObj(left, 830, right, 890);
        mTextReset.setText("RESET");

        mTextPlay = new TextObj(left, 920, right, 980);
        mTextPlay.setText("PLAY");

        mTextSettings = new TextObj(left, 1010, right, 1070);
        mTextSettings.setText("SETTINGS");

        setOnTouchListener(this);
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
            if (mTimerHelper.isNextSec()) {
                mRequiresRender = true;
            }

            if (mPlayGame && mTimerHelper.isNextFrameAvailable()) {
                mGameBoard.game();
                mTimerHelper.setStateTime();

                mRequiresRender = true;
            }

            if (mRequiresRender) {
                Canvas c = mHolder.lockCanvas();
                onDraw(c);
                mHolder.unlockCanvasAndPost(c);
                mRequiresRender = false;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFFFBB33);

        mGameBoard.onDraw(canvas);
        mTextRandom.onDraw(canvas);
        mTextReset.onDraw(canvas);
        mTextPlay.onDraw(canvas);
        mTextSettings.onDraw(canvas);

        canvas.drawText(String.valueOf(mTimerHelper.getTimeInSec()), 380, 32, mPaintTime);
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
                mGameBoard.isClicked(x, y);

                if (mTextReset.isClicked(x, y)) {
                    mGameBoard.resetBoard();
                } else if (mTextRandom.isClicked(x, y)) {
                    mGameBoard.randomBoard();
                } else if (mTextPlay.isClicked(x, y)) {
                    playGame();
                } else if (mTextSettings.isClicked(x, y)) {
                    mSurfaceListener.openDrawer();
                }

                mRequiresRender = true;

                //endTouchPut();
                break;

            case MotionEvent.ACTION_MOVE:
                //moveTouch();
                break;
        }
        return true;
    }

    private void playGame() {
        mPlayGame = !mPlayGame;
        if (mPlayGame) {
            mTextPlay.setText("STOP");
        } else {
            mTextPlay.setText("PLAY");
        }
    }

    public void setSurfaceListener(SurfaceListener surfaceListener) {
        mSurfaceListener = surfaceListener;
    }

    public void updateBoard(int row, int column) {
        // size and location board
        int sizeBoard = (int) ((float) mWidthScreen * 0.8f);
        int offsetX = (int) (mWidthScreen * 0.1f);
        int offsetY = (int) (mWidthScreen * 0.1f);
        mGameBoard = new GameBoard(sizeBoard, offsetX, offsetY, row, column);

        mRequiresRender = true;
    }

    public void setUpdateInterval(int updateTime) {
        mTimerHelper.setUpdateInterval(updateTime);
    }

}
