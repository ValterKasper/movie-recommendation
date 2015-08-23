package sk.kasper.movieapp.movie;

/**
 * Represents UI for movie suggestion.
 */
public interface IMovieView {
    void showProgressBar();

    void hideProgressBar();

    void addMovieCard(Movie movie);
}
