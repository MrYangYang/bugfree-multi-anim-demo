package ml.uname.app.nearbyquestionactivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;


public class NearbyQuestionActivity extends Activity {


    RadarDrawable drawable;
    ImageView imageView;
    FrameLayout container;
    ListView listview;
    ImageView avatarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_question);
        drawable = new RadarDrawable(2000, Color.parseColor("#aaaaaa"), Color.parseColor("#ffffff"), 300);
        drawable.setStopCallback(new Callback());
        imageView = (ImageView) findViewById(R.id.imageview);
        imageView.setBackgroundDrawable(drawable);
        container = (FrameLayout) findViewById(R.id.container);
        listview = (ListView) findViewById(R.id.list);
        avatarImageView = (ImageView) findViewById(R.id.avatar);
    }

    public void start(View v) {
        drawable.start();
    }

    public void stop(View v) {
        drawable.stop();
//        mAnimateView.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nearby_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class Callback implements StopCallback {

        @Override
        public void onStop() {
            imageView.setBackgroundDrawable(null);
            int padding = imageView.getPaddingLeft();
            int l = imageView.getLeft() + padding;
            int t = imageView.getTop() + padding;
            int w = imageView.getWidth();
            int h = imageView.getHeight();
            int r = l + w - 2 * padding;
            int b = t + h - 2 * padding;
            imageView.layout(l, t, r, b);
            imageView.invalidate();
            imageView.setPadding(0, 0, 0, 0);
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    ViewPropertyAnimator animator = ViewPropertyAnimator.animate(imageView);
                    animator.setDuration(500)
                            .xBy(-imageView.getLeft())
                            .yBy(-imageView.getTop())
                            .scaleX((float) avatarImageView.getWidth() / imageView.getWidth())
                            .scaleY((float) avatarImageView.getHeight() / imageView.getHeight())
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    imageView.setVisibility(View.GONE);
                                    container.setVisibility(View.VISIBLE);
                                    View v = container.findViewById(R.id.head);
                                    v.bringToFront();
                                    v.findViewById(R.id.avatar).setBackgroundDrawable(new HarfTransparentDrawable());
                                    int x0 = avatarImageView.getWidth() / 2;
                                    LineDrawable drawable1 = new LineDrawable(5, 500, new Point(x0, 0), new Point(x0, listview.getHeight()), Color.parseColor("#cccccc"));
                                    drawable1.setCallback(new DrawLineCallback());
                                    container.setBackgroundDrawable(drawable1);
                                    drawable1.start();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            })
                            .start();
                }
            });
        }
    }


    private class DrawLineCallback implements StopCallback {

        @Override
        public void onStop() {
            final ListAdapter adapter = new ListAdapter();
            listview.setEnabled(false);
            listview.setAdapter(adapter);
            listview.setVisibility(View.VISIBLE);

            listview.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        }
    }


    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            int f = listview.getFirstVisiblePosition();
            int l = listview.getLastVisiblePosition();
            View v;
            List<View> views = new ArrayList<View>();
            for (int i = f; i <= l; i++) {
                v = listview.getChildAt(i).findViewById(R.id.avatar);
                View tmp = listview.getChildAt(i).findViewById(R.id.text);
                if (tmp != null) {
                    ViewHelper.setX(tmp, tmp.getX() + tmp.getWidth());
                    views.add(tmp);
                }
                ViewHelper.setScaleX(v, 0);
                ViewHelper.setScaleY(v, 0);
                ViewPropertyAnimator animator;
                animator = ViewPropertyAnimator.animate(v);
                animator.scaleX(1)
                        .scaleY(1)
                        .setDuration(500);
                if (i == l) {
                    animator.setListener(new ScaleAnimationListener(views));
                }
                animator.start();
            }
        }
    };


    class ScaleAnimationListener implements Animator.AnimatorListener {

        private List<View> views;

        ScaleAnimationListener(List<View> views) {
            this.views = views;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(final Animator animation) {
            int delay = 0;
            int step = 200;

            Handler handler = new Handler();
            for (int i = 0; i < views.size(); i++) {
                final View view = views.get(i);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ViewPropertyAnimator animator = ViewPropertyAnimator.animate(view)
                                .x(view.getX() - view.getWidth())
                                .setDuration(200);
                        if (view.equals(views.get(views.size() - 1))) {
                            animator.setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    listview.setEnabled(true);
                                    if (Build.VERSION.SDK_INT >= 14) {
                                        listview.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
                                    }
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            animator.start();
                        }
                    }
                }, delay);
                delay += step;
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    private class ListAdapter extends BaseAdapter {


        boolean mInitAnimEnd = false;

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            if (getItemViewType(position) == 0) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(NearbyQuestionActivity.this).inflate(R.layout.list_item_head, parent, false);
                    convertView.setVisibility(View.INVISIBLE);
                }
                return convertView;
            }

            if (convertView == null) {
                convertView = LayoutInflater.from(NearbyQuestionActivity.this).inflate(R.layout.list_item, parent, false);
            }


            ((TextView) convertView.findViewById(R.id.text)).setText(position + "");
//            if (!mInitAnimEnd) {
//                convertView.setVisibility(View.INVISIBLE);
//            } else {
//                convertView.setVisibility(View.VISIBLE);
//            }
            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return 0;
            else
                return 1;
        }

    }


}
