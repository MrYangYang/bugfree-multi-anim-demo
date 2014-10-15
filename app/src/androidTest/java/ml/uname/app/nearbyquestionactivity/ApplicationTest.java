package ml.uname.app.nearbyquestionactivity;

import android.app.Application;
import android.graphics.Color;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testColor() {
        int color = Color.parseColor("#ffffff");
        System.out.println(Color.red(color));
    }
}