package sk.kasper.movieapp.api;

import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import sk.kasper.movieapp.models.Item;

/**
 * Created by Valter on 01.08.2015.
 */
public class NotTodayService {
	private NotTodayApi notTodayApi;

	public NotTodayService(final NotTodayApi notTodayApi) {
		this.notTodayApi = notTodayApi;
	}

	public void onEvent(Requests.LoadItemsRequest request) {
		notTodayApi.loadItems(new Callback<List<Item>>() {
			@Override
			public void success(final List<Item> items, final Response response) {
				EventBus.getDefault().post(items);
			}

			@Override
			public void failure(final RetrofitError error) {
			}
		});
	}
}
