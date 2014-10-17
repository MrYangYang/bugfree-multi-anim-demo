package ml.uname.app.nearbyquestionactivity;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;

/**
 * Created by luyangsheng on 2014/10/16.
 */
public class LineDrawable extends Drawable implements Animatable {

    private Paint mPaint;
    private int mDuration;
    private Point mPosStart;
    private Point mPosEnd;
    private boolean mIsRunning = false;
    private boolean mIsComplete = false;
    private long mStartTime = 0;
    private int count = -1;
    private StopCallback mCallback;
    private Handler mHandler = new Handler();

    public LineDrawable(int lineWidth, int duration, Point posStart, Point posEnd, int color) {
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mDuration = duration;
        this.mPosStart = posStart;
        this.mPosEnd = posEnd;
        mPaint.setColor(color);
        mPaint.setStrokeWidth(lineWidth);
    }

    public void setCallback(StopCallback callback) {
        mCallback = callback;
    }

    @Override
    public void draw(Canvas canvas) {
        count++;
        if (mIsRunning) {

            long curFrame = System.currentTimeMillis() - mStartTime;
            float factor = (float) curFrame / mDuration;
            if (factor >= 1.0f) {
                mIsRunning = false;
                mIsComplete = true;
                if (mCallback != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onStop();
                        }
                    });
                }
            }
            int curPosX = (int) ((mPosEnd.x - mPosStart.x) * factor + mPosStart.x);
            int curPosY = (int) ((mPosEnd.y - mPosStart.y) * factor + mPosStart.y);
            canvas.drawLine(mPosStart.x, mPosStart.y, curPosX, curPosY, mPaint);
            invalidateSelf();
        }

        if (mIsComplete) {
            canvas.drawLine(mPosStart.x, mPosStart.y, mPosEnd.x, mPosEnd.y, mPaint);
        }
    }


    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    public void start() {
        if (!mIsRunning) {
            mIsRunning = true;
            mIsComplete = false;
            mStartTime = System.currentTimeMillis();
            invalidateSelf();
        }
    }

    @Override
    public void stop() {
        if (mIsRunning) {
            mIsRunning = false;
        }
    }

    @Override
    public boolean isRunning() {
        return mIsRunning;
    }
}
