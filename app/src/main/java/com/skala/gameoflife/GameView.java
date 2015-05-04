package com.skala.gameoflife;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.skala.gameoflife.surfaceobject.GameBoard;
import com.skala.gameoflife.surfaceobject.TextObj;

/**
 * @author Skala
 */

public class GameView extends SurfaceView implements Runnable, View.OnTouchListener {
    private Thread mThreadGame;
    private boolean mRunningGame = false;
    private SurfaceHolder mHolder;
    private Paint mPaintTime = new Paint();
    private Paint mPaintFPS = new Paint();

    private TimerHelper mTimerHelper;
    private GameBoard mGameBoard;

    private int mWidthScreen;
    private int mHeightScreen;

    private TextObj mTextReset;
    private TextObj mTextRandom;
    private TextObj mTextPlay;

    private boolean mPlayGame = false;
    private boolean mRequiresRender = true;

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

        // size and location board
        int sizeBoard = (int) ((float) widthScreen * 0.8f);
        int offsetX = (int) (mWidthScreen * 0.1f);
        int offsetY = (int) (mWidthScreen * 0.1f);

        mGameBoard = new GameBoard(sizeBoard, offsetX, offsetY);

        int halfScreen = mWidthScreen / 2;
        final int widthButton = 90;
        int left = halfScreen - widthButton;
        int right = halfScreen + widthButton;

        mTextRandom = new TextObj(left, 780, right, 840);
        mTextRandom.setText("RANDOM");

        mTextReset = new TextObj(left, 870, right, 930);
        mTextReset.setText("RESET");

        mTextPlay = new TextObj(left, 960, right, 1020);
        mTextPlay.setText("PLAY");

        setOnTouchListener(this);

        mTimerHelper = new TimerHelper();
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
}
