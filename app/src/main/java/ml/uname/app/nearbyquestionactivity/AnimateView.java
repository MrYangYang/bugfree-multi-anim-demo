package ml.uname.app.nearbyquestionactivity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Transformation;

import java.util.Objects;

/**
 * Created by luyangsheng on 2014/10/14.
 */
public class AnimateView extends View {

    private Paint mPaint;
    private int mCircleWidth;
    private int mCurRadius;
    private boolean mStop = false;
    private int mMoveStep;
    private int mMaxRadius;

    private int mRedStart;
    private int mGreenStart;
    private int mBlueStart;

    private int mRedEnd;
    private int mGreenEnd;
    private int mBlueEnd;

    private int mSteps;
    private float[] mStepRGB = new float[3];
    private float[] mCurColor = new float[3];

    private int mXCenter;
    private int mYCenter;

    private AccelerateInterpolator interpolator;

    private Thread mAnimateThread;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                invalidate();
            }
        }
    };


    public AnimateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    synchronized public void start() {
        if (mAnimateThread != null)
            return;
        mStop = false;
        mAnimateThread = new AnimateThread();
        mAnimateThread.start();
    }

    synchronized public void stop() {
        if (mAnimateThread == null)
            return;
        mStop = true;
        mAnimateThread = null;
    }

    public AnimateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimateView);
        int startColor = typedArray.getColor(R.styleable.AnimateView_StartColor, Color.BLACK);
        mRedStart = Color.red(startColor);
        mGreenStart = Color.green(startColor);
        mBlueStart = Color.blue(startColor);

        int endColor = typedArray.getColor(R.styleable.AnimateView_EndColor, Color.WHITE);
        mRedEnd = Color.red(endColor);
        mGreenEnd = Color.green(endColor);
        mBlueEnd = Color.blue(endColor);

        typedArray.recycle();

        mCircleWidth = typedArray.getDimensionPixelSize(R.styleable.AnimateView_CircleWidth, 0);
        if (mCircleWidth == 0)
            throw new IllegalStateException("圆环宽度不能为0");

        interpolator = new AccelerateInterpolator(0.3f);
        interpolator.getInterpolation(100);

        mCurRadius = 1;
        mCurColor[0] = mRedStart;
        mCurColor[1] = mGreenStart;
        mCurColor[2] = mBlueStart;
        mMoveStep = 3;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            mMaxRadius = Math.min(getWidth(), getHeight()) / 2;
            mSteps = mMaxRadius / mCircleWidth + 1;
            if (mSteps == 0)
                return;
            mStepRGB[0] = ((float) mRedStart - mRedEnd) / mSteps / (((float) mCircleWidth) / mMoveStep) - 1;
            mStepRGB[1] = ((float) mGreenStart - mGreenEnd) / mSteps / (((float) mCircleWidth) / mMoveStep) - 1;
            mStepRGB[2] = ((float) mBlueStart - mBlueEnd) / mSteps / (((float) mCircleWidth) / mMoveStep) - 1;
            mXCenter = (right - left) / 2;
            mYCenter = (bottom - top) / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        int color = Color.rgb((int) mCurColor[0], (int) mCurColor[1], (int) mCurColor[2]);
        mPaint.setColor(color);

        if (mSteps == 0)
            return;

        if (mCurRadius >= mCircleWidth) {
            mCurColor[0] = mRedStart;
            mCurColor[1] = mGreenStart;
            mCurColor[2] = mBlueStart;
            mCurRadius = 1;
        }

        // 内环
        canvas.drawCircle(mXCenter, mYCenter, mCurRadius, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleWidth);

        float[] colors = new float[3];
        float[] tmp = new float[3];
        colors[0] = ((float) mRedStart - mRedEnd) / mSteps;
        colors[1] = ((float) mGreenStart - mGreenEnd) / mSteps;
        colors[2] = ((float) mBlueStart - mBlueEnd) / mSteps;
        for (int j = 0; j < 3; j++) {
            tmp[j] = mCurColor[j];
        }
        int i;
        for (i = mCurRadius + mCircleWidth / 2 - 1; i < mMaxRadius - mCircleWidth; i += (mCircleWidth - 1)) {
            for (int j = 0; j < 3; j++) {
                tmp[j] -= colors[j];
            }
            mPaint.setColor(Color.rgb((int) tmp[0], (int) tmp[1], (int) tmp[2]));
            canvas.drawCircle(mXCenter, mYCenter, i, mPaint);
        }

//        int tmp_pos = (i - mCircleWidth) + mCircleWidth / 2 + (mMaxRadius - i) / 2;
//        int strokeWidth = (mMaxRadius - i);
//        for (int j = 0; j < 3; j++) {
//            tmp[j] -= colors[j];
//        }
//        mPaint.setColor(Color.rgb((int) tmp[0], (int) tmp[1], (int) tmp[2]));
//        mPaint.setStrokeWidth(strokeWidth);
//        canvas.drawCircle(mXCenter, mYCenter, tmp_pos, mPaint);
    }

    private class AnimateThread extends Thread {
        @Override
        public void run() {
            while (!mStop) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mCurRadius += mMoveStep;
                for (int i = 0; i < 3; i++)
                    mCurColor[i] -= mStepRGB[i];
                mHandler.sendEmptyMessage(0);
            }
        }
    }
}
