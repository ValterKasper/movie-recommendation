/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Valter Kasper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package sk.kasper.movieapp.ui.movie;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.network.OmdbApi;
import sk.kasper.movieapp.network.TasteKidApi;
import sk.kasper.movieapp.storage.BookmarksStorage;
import sk.kasper.movieapp.storage.MoviesStorage;

/**
 * Manipulates with UI
 */
public class MoviePresenter {

    private static final int MINIMUM_MOVIES_COUNT_THRESHOLD = 3;
    private static final int LIMIT_OF_SUGGESTIONS = 3;
    private static final Movie[] SEED_MOVIES = {new Movie(1L, "Up!"), new Movie(2L, "Matrix"), new Movie(3L, "Rush")};
    private final TasteKidApi tasteKidApi;
    private final OmdbApi omdbApi;
    private final BookmarksStorage bookmarksStorage;
    private MoviesStorage moviesStorage;
    private IMovieView movieView;
    private String tastekidApiKey;
    private boolean noMovieIsShown = true;
    private int seedMoviesIndex = 0;
    private List<Movie> shownMovies;
    private List<Movie> dislikedMovies;
    private Queue<Movie> likedMoviesQueue;

    /**
     * Movies prepared to be shown
     */
    private Queue<Movie> movieQueue = new ArrayDeque<>();

    public MoviePresenter(IMovieView movieView, TasteKidApi tasteKidApi, OmdbApi omdbApi, final String tastekidApiKey, final BookmarksStorage bookmarksStorage, MoviesStorage moviesStorage) {
        this.movieView = movieView;
        this.tasteKidApi = tasteKidApi;
        this.omdbApi = omdbApi;
        this.tastekidApiKey = tastekidApiKey;
        this.bookmarksStorage = bookmarksStorage;
        this.moviesStorage = moviesStorage;

        this.shownMovies = moviesStorage.loadShownMovies();
        this.likedMoviesQueue = new LinkedList<>(moviesStorage.loadLikedMovies());
        this.dislikedMovies = moviesStorage.loadDislikedMovies();
        this.movieQueue = new LinkedList<>(moviesStorage.loadMoviesToBeShown());

        movieView.getMovieLikeStream().subscribe(like -> {
            onLikeMovie(like.getMovie());
        });

        movieView.getMovieDislikeStream().subscribe(dislike -> {
            onDislikeMovie(dislike.getMovie());
        });

		movieView.getMovieBookmarkToggleStream().subscribe(bookmarkToggle -> {
			onBookmarkMovie(bookmarkToggle.movie);
		});
	}

	private boolean isMoreMoviesToRetrieveRecommendations() {
		return true;
	}

	private Movie getNextMovieToRetrieveRecommendations() {
		if (likedMoviesQueue.isEmpty()) {
			return SEED_MOVIES[seedMoviesIndex++];
		} else {
			return likedMoviesQueue.remove();
		}
	}

    public void onResume() {
        if (moreMoviesToViewAreNeeded()) {
            getMovieSuggestions();
        } else {
            showNextMovieInView();
        }
    }

    public void movieRecommendation(Movie movie) {
        movieQueue.add(movie);

        if (noMovieIsShown) {
            Log.d("FOO", "subscribeSuggestionStream no movie shown");
            showNextMovieInView();
        }
    }

    public void onPause() {
        moviesStorage.saveLikedMovies(new ArrayList<>(likedMoviesQueue));
        moviesStorage.saveDislikedMovies(dislikedMovies);
        moviesStorage.saveShownMovies(shownMovies);
        moviesStorage.saveMoviesToBeShown(new ArrayList<>(movieQueue));
    }

    private void onLikeMovie(Movie movie) {
        likedMoviesQueue.add(movie);
        shownMovies.add(movie);
        movieQueue.remove();
        showNextMovieInView();
	}

    private void onDislikeMovie(Movie movie) {
        dislikedMovies.add(movie);
        shownMovies.add(movie);
        movieQueue.remove();
        showNextMovieInView();
	}

	private void onBookmarkMovie(final Movie movie) {
		movie.bookmarked = !movie.bookmarked;
		movieView.showAsBookmarked(movie.bookmarked);
        bookmarksStorage.bookmarkMovie(movie);
    }

	private void showNextMovieInView() {
        movieView.showNextMovie(movieQueue.element());
        noMovieIsShown = false;

        if (moreMoviesToViewAreNeeded()) {
            getMovieSuggestions();
        }
    }

	/**
	 * Loads movies only if there isn't enough of them
     */
    private void getMovieSuggestions() {
        tasteKidApi.loadRecommendations(getNextMovieToRetrieveRecommendations().name, LIMIT_OF_SUGGESTIONS, tastekidApiKey) // load recommendations
                .map(tasteKidResponse -> tasteKidResponse.Similar.Results)
                .flatMap(Observable::from)
                .flatMap(tasteKidRespItem -> omdbApi.getDetailOfMovie(tasteKidRespItem.Name)) // get detail of movie
                .flatMap(omdbResp -> Observable.just(new Movie( // create movie stream
                        parseImdbId(omdbResp.imdbID),
                        omdbResp.Title,
                        omdbResp.Poster,
                        omdbResp.Plot,
                        omdbResp.imdbRating,
                        omdbResp.Metascore,
                        omdbResp.Genre,
                        omdbResp.Actors,
                        omdbResp.Director,
                        omdbResp.Country)))
                .filter(movie -> !shownMovies.contains(movie))
                .limit(LIMIT_OF_SUGGESTIONS) // enough is enough
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(this::movieRecommendation);
    }

    private Long parseImdbId(final String imdbID) {
        final String substring = imdbID.substring(2);
        return Long.parseLong(substring);
    }

    private boolean moreMoviesToViewAreNeeded() {
        return movieQueue.size() < MINIMUM_MOVIES_COUNT_THRESHOLD;
    }
}
