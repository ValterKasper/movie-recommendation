package sk.kasper.movieapp.api;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import rx.Observable;
import sk.kasper.movieapp.models.Item;

/**
 * Created by Valter on 01.08.2015.
 */
public interface MovieApi {
    String REST_APIARY_ENDPOINT = "http://private-e16bf-nottoday.apiary-mock.com";
    String REST_TASTEKID_ENDPOINT = "https://www.tastekid.com/api/";

	@GET("/items")
	void loadItems(Callback<List<Item>> callback);

	@GET("/items")
	Observable<List<Item>> loadItems();

    @GET("https://www.tastekid.com/api/similar?q=rush&type=movies")
    Observable<TasteKidResponse> loadMovies();

    class TasteKidResponse {
        Similar Similar;

        public class Similar {
            List<DataItem> Info;
            List<DataItem> Results;

            public class DataItem {
                String Name;
                String Type;
            }
        }

    }
}
