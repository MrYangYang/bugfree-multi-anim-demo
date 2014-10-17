package ml.uname.app.nearbyquestionactivity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;


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
                                    listview.setBackgroundDrawable(drawable1);
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
            AlphaInAnimationAdapter adapter = new AlphaInAnimationAdapter(new ListAdapter());
            adapter.setAbsListView(listview);
            listview.setAdapter(adapter);
        }
    }

    private class ListAdapter extends BaseAdapter {

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
