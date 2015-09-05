package sk.kasper.movieapp;

import android.util.Log;

import retrofit.RestAdapter;
import sk.kasper.movieapp.api.MovieApi;

/**
 * Created by Valter on 01.08.2015.
 */
public class Utils {

	public static MovieApi getApi() {
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(MovieApi.REST_TASTEKID_ENDPOINT)
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setLog(new RestAdapter.Log() {
					@Override
					public void log(String msg) {
						Log.d("Retrofit: ", msg);
					}
				})
				.build();

		return restAdapter.create(MovieApi.class);
	}

}
