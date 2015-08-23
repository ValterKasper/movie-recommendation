package sk.kasper.nottoday.movie;

import rx.Observable;

/**
 * Provides movie data
 */
public class MovieInteractor implements IMovieInteractor {
    @Override
    public Observable<Movie> loadMovie(int id) {
        Movie movie = new Movie();
        movie.name = "Rush";
        return Observable.just(movie);
    }
}
