package sk.kasper.movieapp.movie;

import rx.Observable;

/**
 * Describes interface of suggestion engine.
 */
public interface IMovieSuggestionEngineInteractor {
    Observable<Movie> getNextSuggestion();

    void movieLiked(Movie movie);

    void movieDisliked(Movie movie);
}
