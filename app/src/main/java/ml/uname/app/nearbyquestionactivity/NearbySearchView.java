package ml.uname.app.nearbyquestionactivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by luyangsheng on 2014/10/14.
 */
public class NearbySearchView extends FrameLayout {

    private int mSpaceLeft;
    private int mSpaceRight;
    private int mSpaceTop;
    private int mSpaceBottom;

    public NearbySearchView(Context context) {
        super(context);
    }

    public NearbySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NearbySearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (getChildCount() != 1) {
            throw new IllegalStateException("try to append multi child view to NearbySearchView ");
        }
    }
}
