package ml.uname.app.nearbyquestionactivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by luyangsheng on 2014/10/16.
 */
public class HarfTransparentDrawable extends Drawable {

    private Paint mPaint;
    public HarfTransparentDrawable() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();
        canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom / 2, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
