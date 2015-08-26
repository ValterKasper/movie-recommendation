package sk.kasper.movieapp.movie;

import rx.Subscriber;

/**
 * Manipulates with UI
 */
public class MoviePresenter {
    IMovieSuggestionEngineInteractor movieInteractor;
    private IMovieView movieView;

    public MoviePresenter(IMovieView movieView, IMovieSuggestionEngineInteractor movieInteractor) {
        this.movieView = movieView;
        this.movieInteractor = movieInteractor;
    }

    public void onResume() {
        loadNextMovie();
        loadNextMovie();
        loadNextMovie();
    }

    private void loadNextMovie() {
        movieInteractor.getNextSuggestion()
                .subscribe(new Subscriber<Movie>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Movie movie) {
                        movieView.addMovieCard(movie);
                    }
                });
    }

    public void onLikeMovie(Movie movie) {
        movieInteractor.movieLiked(movie);
        loadNextMovie();
    }

    public void onDislikeMovie(Movie movie) {
        movieInteractor.movieDisliked(movie);
        loadNextMovie();
    }
}
