package sk.kasper.movieapp.activities.takes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import sk.kasper.movieapp.R;
import sk.kasper.movieapp.api.Requests;
import sk.kasper.movieapp.api.Responses;
import sk.kasper.movieapp.models.Item;


/**
 * Zobrazuje zoznam poloziek
 */
public class FooActivity extends AppCompatActivity {
    @Bind(R.id.lvItems)
    ListView lvItems;

    @Bind(R.id.vEmpty)
    TextView vEmpty;

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    private ArrayAdapter<Item> adapter;
    private List<Item> items;

    public static void main(boolean v1, boolean v2) {
        if (v1) {
            Log.d("AA", "main ");
        } else {
            if (v2) {
                if (v1) {
                    Log.d("AA", "main ");
                    if (v2) {
                        if (v1 && v2) {
                            Log.d("AA", "main ");
                        } else {
                            Log.d("AA", "main ");
                            int a = v1 ? 5 : 6;
                        }
                    }
                }
                Log.d("AA", "main ");
            }
        }

        if ("asd".isEmpty()) {
            Log.d("AA", "main ");
        }

        if ("asd".isEmpty()) {
            Log.d("AA", "main ");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foo);
        ButterKnife.bind(this);
        items = new ArrayList<Item>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(adapter);
        swapVisibilityOfContentViews();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //registerForContextMensu(lvItems);
    }

    private void swapVisibilityOfContentViews() {
        if (items.isEmpty()) {
            lvItems.setVisibility(View.GONE);
            vEmpty.setVisibility(View.VISIBLE);
        } else {
            lvItems.setVisibility(View.VISIBLE);
            vEmpty.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.bAddItem)
    public void buttonClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Add item");
        builder.setTitle("Select item");
        builder.setPositiveButton("OK",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.setNegativeButton("No",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new Requests.LoadItemsRequest());
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_foo, menu);
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

    public void onEvent(Responses.LoadItemsResponse response) {
        if (!response.items.isEmpty()) {
            items.clear();
            items.addAll(response.items);
            adapter.notifyDataSetChanged();
            swapVisibilityOfContentViews();
        }
    }

    public void onEvent(Responses.DeleteItemResponse deleteItemResponse) {
        Item itemToDelete = new Item();
        itemToDelete.id = deleteItemResponse.id;
        items.remove(itemToDelete);
        adapter.notifyDataSetChanged();

        swapVisibilityOfContentViews();
    }
}
