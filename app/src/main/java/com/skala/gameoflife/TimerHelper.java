package com.skala.gameoflife;

import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * @author Skala
 */

public class TimerHelper {
    private final static long INTERVAL = 200;

    private long mTimeStart = 0;
    private String mFPS = "";
    private DecimalFormat mDF = new DecimalFormat("#.#");
    private long mTimeSec = 0;
    private long mPreviousCheckTimeSec = 0;
    private long mTimeMilis;
    private long mPreviousStateTimeMilis = 0;

    private LinkedList<Double> mTimes = new LinkedList<>();
    private final static int MAX_SIZE = 100;
    private final static double NANOS = 1000000000.0;

    public void onResume() {
        mTimeStart = System.currentTimeMillis();
        mTimes.addLast((double) System.nanoTime());
    }

    public void nextFrame() {
        mTimeMilis = System.currentTimeMillis();
        mTimeSec = (mTimeMilis - mTimeStart) / 1000;
        //mFPS = mDF.format(getFPS());
    }

    public boolean isNextFrameAvailable() {
        return mPreviousStateTimeMilis + INTERVAL < mTimeMilis;
    }

    public boolean isNextSec() {
        boolean value = mTimeSec != mPreviousCheckTimeSec;
        mPreviousCheckTimeSec = mTimeSec;
        return value;
    }

    public void setStateTime() {
        mPreviousStateTimeMilis = mTimeMilis;
    }

    public long getTimeInSec() {
        return mTimeSec;
    }

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

    public String getFPSParse() {
        return mFPS;
    }
}
