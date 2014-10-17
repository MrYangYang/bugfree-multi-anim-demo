package ml.uname.app.nearbyquestionactivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;

/**
 * 附近的提问的搜索动画效果
 * Created by luyangsheng on 2014/10/15.
 */
public class RadarDrawable extends Drawable implements Animatable {

    private boolean mIsRunning;
    private Paint mPaint;
    private boolean mIsStopping;
    private long mStopTime;
    private int mCircleWidth;
    private int mCurRadius;
    private int mDuration;
    private short[] mStartColors = new short[3];
    private short[] mEndColors = new short[3];
    private float[] mTmpColor = new float[3];
    private long mStart;
    private StopCallback mCallback;
    private Handler mHandler = new Handler();

    public RadarDrawable(int duration, int startColor, int endColor, int circleWidth) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStart = System.currentTimeMillis();
        mIsRunning = true;
        mIsStopping = false;

        mStartColors[0] = (short) Color.red(startColor);
        mStartColors[1] = (short) Color.green(startColor);
        mStartColors[2] = (short) Color.blue(startColor);

        mEndColors[0] = (short) Color.red(endColor);
        mEndColors[1] = (short) Color.green(endColor);
        mEndColors[2] = (short) Color.blue(endColor);

        mCircleWidth = circleWidth;
        mDuration = duration;
    }


    public void setStopCallback(StopCallback cb) {
        mCallback = cb;
    }

    /**
     *
     */
    @Override
    public void start() {
        if (!mIsRunning) {
            mStart = System.currentTimeMillis();
            mIsRunning = true;
            mIsStopping = false;
            invalidateSelf();
        }
    }

    @Override
    public void stop() {
        if (mIsRunning) {
            mIsRunning = false;
            mIsStopping = true;
            mStopTime = System.currentTimeMillis();
        }
    }

    @Override
    public boolean isRunning() {
        return mIsRunning;
    }

    public boolean isStopping() {
        return mIsStopping;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();
        int maxRadius = Math.min(rect.width(), rect.height()) / 2;
        int cx = rect.centerX();
        int cy = rect.centerY();

        // 圆环值不合理时
        if (mCircleWidth > maxRadius) {
            mCircleWidth = maxRadius / 3;
        }

        if (mIsRunning) {
            long curFrame = (System.currentTimeMillis() - mStart);
            mCurRadius = ((int) ((double) curFrame / mDuration * maxRadius)) % mCircleWidth;
            int circles = (int) Math.ceil((float) maxRadius / mCircleWidth);
            for (int i = 0; i < 3; i++) {
                mTmpColor[i] = getColor(i, mCurRadius, maxRadius);
            }

            // 绘制实心的圈圈
            mPaint.setStyle(Paint.Style.FILL);
            setPaintColor(mPaint, mTmpColor[0], mTmpColor[1], mTmpColor[2]);
            canvas.drawCircle(cx, cy, mCurRadius, mPaint);

            // 绘制其他的满宽度圆环
            int limit = maxRadius - mCircleWidth / 2;
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mCircleWidth);
            int curRadius;
            for (curRadius = mCurRadius + mCircleWidth / 2 - 1; curRadius < limit; curRadius += mCircleWidth) {
                for (int i = 0; i < 3; i++) {
                    mTmpColor[i] = getColor(i, curRadius, maxRadius, mCircleWidth);
                }
                fixColor();
                setPaintColor(mPaint, mTmpColor[0], mTmpColor[1], mTmpColor[2]);
                canvas.drawCircle(cx, cy, curRadius, mPaint);
            }

            // 绘制最后一个不满宽度的圆环
            curRadius = (curRadius - mCircleWidth / 2 + maxRadius) / 2;
            int circleWidth = (maxRadius - curRadius) * 2;
            for (int i = 0; i < 3; i++) {
                mTmpColor[i] = getColor(i, curRadius, maxRadius, circleWidth);
            }
            fixColor();
            setPaintColor(mPaint, mTmpColor[0], mTmpColor[1], mTmpColor[2]);
            mPaint.setStrokeWidth(circleWidth);
            canvas.drawCircle(cx, cy, curRadius, mPaint);
            invalidateSelf();
            // 正在停止
        } else if (mIsStopping) {
            long now = System.currentTimeMillis();
            int curRadius = mCurRadius + (int) ((float) (now - mStopTime) / mDuration * maxRadius);
            if (curRadius >= maxRadius) {
                // 已经停止
                mIsStopping = false;
                canvas.drawColor(Color.rgb(mEndColors[0], mEndColors[1], mEndColors[2]));
                if (mCallback != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onStop();
                        }
                    });
                }
                return;
            }

            for (int i = 0; i < 3; i++) {
                mTmpColor[i] = getColor(i, curRadius, maxRadius);
            }

            // 绘制实心的圈圈
            fixColor();
            mPaint.setStyle(Paint.Style.FILL);
            setPaintColor(mPaint, mTmpColor[0], mTmpColor[1], mTmpColor[2]);
            canvas.drawCircle(cx, cy, curRadius, mPaint);

            // 绘制其他的满宽度圆环
            int limit = maxRadius - mCircleWidth / 2;
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mCircleWidth);
            for (curRadius += mCircleWidth / 2 - 1; curRadius < limit; curRadius += mCircleWidth) {
                for (int i = 0; i < 3; i++) {
                    mTmpColor[i] = getColor(i, curRadius, maxRadius, mCircleWidth);
                }
                fixColor();
                setPaintColor(mPaint, mTmpColor[0], mTmpColor[1], mTmpColor[2]);
                canvas.drawCircle(cx, cy, curRadius, mPaint);
            }

            // 绘制最后一个不满宽度的圆环
            curRadius = (curRadius - mCircleWidth / 2 + maxRadius) / 2;
            int circleWidth = (maxRadius - curRadius) * 2;
            for (int i = 0; i < 3; i++) {
                mTmpColor[i] = getColor(i, curRadius, maxRadius, circleWidth);
            }
            fixColor();
            setPaintColor(mPaint, mTmpColor[0], mTmpColor[1], mTmpColor[2]);
            mPaint.setStrokeWidth(circleWidth);
            canvas.drawCircle(cx, cy, curRadius, mPaint);
            invalidateSelf();
        }
    }


    /**
     * 获得圆环的颜色
     * @param rgb 0->red, 1->green, 2->blue
     * @param curRadius 当前的半径
     * @param maxRadius 半径限制
     * @param strokeWidth 圆环宽度
     * @return 圆环的颜色
     */
    private int getColor(int rgb, int curRadius, int maxRadius, int strokeWidth) {
        return (int)((curRadius + (float)strokeWidth / 2) / maxRadius * (mEndColors[rgb] - mStartColors[rgb]) + mStartColors[rgb]);
    }


    /**
     * 获得实心的圆的颜色
     * @param rgb
     * @param curRadius 圆的半径
     * @param maxRadius 最大半径
     * @return
     */
    private int getColor(int rgb, int curRadius, int maxRadius) {
        return getColor(rgb, curRadius, maxRadius, 0);
    }

    private void fixColor() {
        for (int i = 0; i < 3; i++) {
            if (mStartColors[i] < mEndColors[i]) {
                if (mTmpColor[i] > mEndColors[i])
                    mTmpColor[i] = mEndColors[i];
            } else {
                if (mTmpColor[i] < mEndColors[i])
                    mTmpColor[i] = mEndColors[i];
            }
        }
    }

    private void setPaintColor(Paint p, float r, float g, float b) {
        int color = Color.rgb((int) r, (int) g, (int) b);
        p.setColor(color);
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

}
