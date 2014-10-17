package ml.uname.app.nearbyquestionactivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

/**
 * Created by luyangsheng on 2014/10/15.
 */
public class TestActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    private class MyView extends View {
        Paint p;

        public MyView(Context context) {
            super(context);
            p = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            p.setColor(Color.RED);
            p.setStyle(Paint.Style.FILL);
            p.setStrokeWidth(400);
            canvas.drawCircle(cx, cy, 200, p);
        }
    }
}
