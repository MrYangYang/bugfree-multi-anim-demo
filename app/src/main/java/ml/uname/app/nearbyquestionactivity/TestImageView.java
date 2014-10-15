package ml.uname.app.nearbyquestionactivity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by luyangsheng on 2014/10/15.
 */
public class TestImageView extends ImageView {

    public TestImageView(Context context) {
        super(context);
    }

    public TestImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }
}
