package sk.kasper.movieapp.ui.movie;


import sk.kasper.movieapp.models.Movie;

public interface IUserInteraction {
    void onLikeMovie(final Movie movie);
    void onDislikeMovie(final Movie movie);
    void onToogleBookmark(final Movie movie);
}
