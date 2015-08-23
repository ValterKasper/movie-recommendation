package sk.kasper.movieapp.movie;

/**
 * Created by Valter on 22.08.2015.
 */
public interface IMovieView {
    void showMovieInfo(Movie movie);

    void showProgressBar();

    void hideProgressBar();
}
