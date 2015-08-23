package sk.kasper.movieapp.movie;

import rx.Observable;

/**
 * Created by Valter on 22.08.2015.
 */
public interface IMovieInteractor {
    Observable<Movie> loadMovie(int id);
}
