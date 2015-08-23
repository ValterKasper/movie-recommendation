package sk.kasper.nottoday.movie;

import javax.inject.Inject;

import rx.Observable;
import sk.kasper.nottoday.Utils;

/**
 * Created by Valter on 22.08.2015.
 */
public class MovieInteractor implements IMovieInteractor {
    @Override
    public Observable<Movie> loadMovie(int id) {
        Movie movie = new Movie();
        movie.name = "Rush";
        return Observable.just(movie);
    }
}
