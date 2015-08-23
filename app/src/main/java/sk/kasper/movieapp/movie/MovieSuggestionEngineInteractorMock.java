package sk.kasper.movieapp.movie;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Provides movie data
 */
public class MovieSuggestionEngineInteractorMock implements IMovieSuggestionEngineInteractor {
    List<Movie> movies = new ArrayList<>();
    private int nextMoviePosition = 0;

    public MovieSuggestionEngineInteractorMock() {
        String[] names = {"Rush", "Bláznivá dovolená", "Cesta vzhůru", "Hitman: Agent 47", "RYTMUS sídliskový sen"};

        for (String name : names) {
            movies.add(new Movie(1L, name));
        }
    }

    @Override
    public Observable<Movie> getNextSuggestion() {
        nextMoviePosition++;
        return Observable.just(movies.get(nextMoviePosition % movies.size()));
    }

    @Override
    public void movieLiked(Movie movie) {

    }

    @Override
    public void movieDisliked(Movie movie) {

    }
}
