package sk.kasper.movieapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import rx.Observable;
import sk.kasper.movieapp.ui.movie.IMovieSuggestionEngineInteractor;
import sk.kasper.movieapp.ui.movie.IMovieView;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.ui.movie.MoviePresenter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MoviePresenterTest {
    private MoviePresenter moviePresenter;
    private IMovieView view;
    private IMovieSuggestionEngineInteractor interactor;

    @Before
    public void setUp() {
        view = mock(IMovieView.class);
        interactor = mock(IMovieSuggestionEngineInteractor.class);
        moviePresenter = new MoviePresenter(view, interactor);
    }

    @Test
    public void likedMessageToInteracotor() {
        Movie movie = new Movie(10L, "Foo");
        when(interactor.getNextSuggestion()).thenReturn(Observable.just(new Movie(2L, "Bar")));
        moviePresenter.onLikeMovie(movie);

        verify(interactor).movieLiked(movie);
    }
}
