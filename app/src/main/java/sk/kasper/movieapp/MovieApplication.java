package sk.kasper.movieapp;

import android.app.Application;

import de.greenrobot.event.EventBus;
import sk.kasper.movieapp.api.MovieApi;
import sk.kasper.movieapp.api.MovieService;

/**
 * Created by Valter on 01.08.2015.
 */
public class MovieApplication extends Application {

	private MovieService movieService;
	private MovieApi movieApi;

	public MovieApi getMovieApi() {
		return movieApi;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		movieApi = Utils.getApi();
		movieService = new MovieService(movieApi);
		EventBus.getDefault().register(movieService);
	}
}
