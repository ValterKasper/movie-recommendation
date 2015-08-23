package sk.kasper.nottoday.activities.takes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import sk.kasper.nottoday.R;

public class ComputeActivity extends AppCompatActivity {
    @Bind(R.id.bReturn)
    Button bReturn;

    @Bind(R.id.tvToCompute)
    TextView tvToCompute;
    private int input;

    @OnClick(R.id.bReturn)
    public void returnClick() {
        EventBus.getDefault().postSticky(new ComputeResult(input * input));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compute);
        ButterKnife.bind(this);
    }

    public void onEvent(ComputeActivity.ComputeRequest request) {
        input = request.value;
        tvToCompute.setText(String.format("%d * %d = %d", input, input, (input * input)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compute, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ComputeRequest {
        public int value;

        public ComputeRequest(int i) {
            value = i;
        }
    }

    public static class ComputeResult {
        public int result;

        public ComputeResult(int result) {
            this.result = result;
        }
    }
}
