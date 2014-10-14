package ml.uname.app.nearbyquestionactivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;

import java.util.Objects;

/**
 * Created by luyangsheng on 2014/10/14.
 */
public class AnimateView extends View {

    private Paint mPaint;
    private int mStep = 100;
    private int mCurRadius = 1;
    private boolean mStop = false;
    private Thread mAnimateThread;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                invalidate();
            }
        }
    };


    public AnimateView(Context context) {
        super(context);
        mPaint = new Paint();
    }

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int mMaxRadius = Math.min(getWidth(), getHeight()) / 2;
        int mCenterX = getWidth() / 2;
        int mCenterY = getHeight() / 2;

        mPaint.setStyle(Paint.Style.FILL);
        int color = 0;
        mPaint.setColor(Color.rgb(color, color, color));

        if (mCurRadius >= mStep)
            mCurRadius = 1;

        // 内环
        canvas.drawCircle(mCenterX, mCenterY, mCurRadius, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStep);
        int i;
        for (i = mCurRadius + mStep / 2 - 1; i < mMaxRadius - mStep; i += (mStep - 1)) {
            color += 20;
            mPaint.setColor(Color.rgb(color, color, color));
            canvas.drawCircle(mCenterX, mCenterY, i, mPaint);
        }

//        int tmp = (i - mStep) + mStep/2 + (mMaxRadius - i)/2;
//        int strokeWidth = (mMaxRadius-i);
//        color += 20;
//        mPaint.setColor(Color.rgb(color, color, color));
//        mPaint.setStrokeWidth(strokeWidth);
//        canvas.drawCircle(mCenterX, mCenterY, tmp, mPaint);
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
                mCurRadius += 1;
                mHandler.sendEmptyMessage(0);
            }
        }
    }
}
