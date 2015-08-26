package sk.kasper.movieapp.movie;

import rx.Observable;


public class MovieSuggestionEngineInteractor implements IMovieSuggestionEngineInteractor {
    @Override
    public Observable<Movie> getNextSuggestion() {
        return Observable.just(new Movie(10L, "LLL"));
    }

    @Override
    public void movieLiked(Movie movie) {

    }

    @Override
    public void movieDisliked(Movie movie) {

    }
}
