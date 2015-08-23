package sk.kasper.nottoday.activities;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import sk.kasper.nottoday.R;


public class MainActivity extends Activity {

	private static final String TAG = "MAIN";

	@Bind(R.id.tvMessage)
	TextView messageTextView;

    @Bind(R.id.tvHello)
    TextView tvHello;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ButterKnife.bind(this);

        tvHello.setText("Hello TDD");
		Log.d(TAG, "onCreate ");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
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

	public void didTapGreetButton(View view) {
		EditText greetEditText =
				(EditText) findViewById(R.id.etGreet);

		String name = greetEditText.getText().toString();
		String greeting = String.format("Hello, %s!", name);

		messageTextView.setText(greeting);

		Log.d(TAG, "TAP");
	}
}