package sk.kasper.movieapp;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import sk.kasper.movieapp.activities.MainActivity;

import static org.assertj.android.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MainActivityTest {
    Button button;
    TextView tvMessage;
    EditText etGreet;
    private MainActivity activity;

    @Before
    public void setup() throws Exception {
        activity = Robolectric.setupActivity(MainActivity.class);
        assertNotNull("MainActivity is not instantiated", activity);
        button = (Button) activity.findViewById(R.id.bGreet);
        tvMessage = (TextView) activity.findViewById(R.id.tvMessage);
        etGreet = (EditText) activity.findViewById(R.id.etGreet);
    }

    @Test
    public void validateTextViewContent() throws Exception {
        TextView tvHelloWorld = (TextView) activity.findViewById(R.id.tvHello);
        assertNotNull("TextView could not be found", tvHelloWorld);
        assertTrue("TextView contains incorrect text",
                "Hello TDD".equals(tvHelloWorld.getText().toString()));
    }

    @Test
    public void clickingButton_shouldChangeResultsViewText() throws Exception {
        etGreet.setText("Jake");
        button.performClick();
        assertEquals(tvMessage.getText().toString(), "Hello, Jake!");
    }

    @Test
    public void layoutHasRightNumberOfViews() throws Exception {
        RelativeLayout rlMain = (RelativeLayout) activity.findViewById(R.id.rlMain);
        assertThat(rlMain).hasChildCount(4);
    }
}