package sk.kasper.nottoday.api;

import java.util.List;


import retrofit.Callback;
import retrofit.http.GET;
import rx.Observable;
import sk.kasper.nottoday.models.Item;

/**
 * Created by Valter on 01.08.2015.
 */
public interface NotTodayApi {
	public static final String REST_ENDPOINT = "http://private-e16bf-nottoday.apiary-mock.com";

	@GET("/items")
	void loadItems(Callback<List<Item>> callback);

	@GET("/items")
	Observable<List<Item>> loadItems();
}
