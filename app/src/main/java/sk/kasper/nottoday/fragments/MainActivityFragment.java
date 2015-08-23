package sk.kasper.nottoday.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;
import sk.kasper.nottoday.api.NotTodayApi;
import sk.kasper.nottoday.NotTodayApplication;
import sk.kasper.nottoday.R;
import sk.kasper.nottoday.api.Requests;
import sk.kasper.nottoday.activities.takes.FooActivity;
import sk.kasper.nottoday.models.Item;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment
		extends Fragment {

//	@Bind(R.id.lvValues)
	ListView lvItems;

//	@Bind(R.id.tvDetail)
	TextView tvDetail;

//	@Bind(R.id.button)
	Button buton;

	private List<Item> itemsList;
	private NotTodayApi notTodayApi;
	private ArrayAdapter<Item> listAdapter;

	public MainActivityFragment() {
	}

//	@OnClick(R.id.button)
	void buttonClick() {
		Intent intent = new Intent(getActivity(), FooActivity.class);
		startActivity(intent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_main, container, false);
//		ButterKnife.bind(this, view);

		itemsList = new ArrayList<>();
		listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, itemsList);
		notTodayApi = ((NotTodayApplication) getActivity().getApplicationContext()).getNotTodayApi();
		lvItems.setAdapter(listAdapter);
		loadItems();

        return view;
	}

	@Override
	public void onPause() {
		super.onPause();

		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onResume() {
		super.onResume();

		EventBus.getDefault().register(this);
	}

//	@OnItemClick(R.id.lvValues)
	void itemClicked(int pos) {
		tvDetail.setText(itemsList.get(pos).toString().substring(0, 20));
	}

	private void loadItems() {
		EventBus.getDefault().post(new Requests.LoadItemsRequest());
	}

	public void onEvent(List<Item> items) {
		itemsList.clear();
		itemsList.addAll(items);
		listAdapter.notifyDataSetChanged();
	}
}
