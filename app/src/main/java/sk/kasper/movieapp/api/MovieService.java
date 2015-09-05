package sk.kasper.movieapp.api;

/**
 * Created by Valter on 01.08.2015.
 */
public class MovieService {

	private MovieApi movieApi;

	public MovieService(final MovieApi movieApi) {
		this.movieApi = movieApi;
	}

	public void onEvent(Object object) {

	}
}
