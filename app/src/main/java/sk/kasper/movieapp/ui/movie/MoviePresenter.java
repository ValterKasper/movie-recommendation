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

import com.squareup.otto.Bus;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.network.OmdbApi;
import sk.kasper.movieapp.network.TasteKidApi;

/**
 * Manipulates with UI
 */
public class MoviePresenter {

    private static final int MINIMUM_MOVIES_COUNT_THRESHOLD = 3;
    private static final int LIMIT_OF_SUGGESTIONS = 3;
    private static final Movie[] SEED_MOVIES = {new Movie(1L, "Up!"), new Movie(2L, "Matrix"), new Movie(3L, "Rush"), new Movie(4L, "Everest")};
    private final TasteKidApi tasteKidApi;
    private final OmdbApi omdbApi;
    private IMovieView movieView;
    private Bus bus;
    private boolean noMovieIsShown = true;
    private int seedMoviesIndex = 0;
    private List<Movie> shownMovies = new ArrayList<>();
    private List<Movie> dislikedMovies = new ArrayList<>();
    private Queue<Movie> likedMoviesQueue = new ArrayDeque<>();
    /**
     * Count of movies prepared to be shown in view
     */
    private int preparedMoviesCount = 0;

    public MoviePresenter(IMovieView movieView, TasteKidApi tasteKidApi, OmdbApi omdbApi) {
        this.movieView = movieView;
        this.tasteKidApi = tasteKidApi;
        this.omdbApi = omdbApi;

        movieView.getMovieLikeStream().subscribe(like -> {
            onLikeMovie(like.getMovie());
        });

        movieView.getMovieDislikeStream().subscribe(dislike -> {
            onDislikeMovie(dislike.getMovie());
        });
    }

    public void onResume() {
        getMovieSuggestionsLazy();
    }

    public void movieRecommendation(Movie movie) {
        preparedMoviesCount++;
        movieView.addMovieCard(movie);

        if (noMovieIsShown) {
            Log.d("FOO", "subscribeSuggestionStream no movie shown");
            showNextMovieInView();
            noMovieIsShown = false;
        }
    }

    public void onPause() {
    }

    private void onLikeMovie(Movie movie) {
        likedMoviesQueue.add(movie);
        shownMovies.add(movie);
        showNextMovieInView();
	}

    private void onDislikeMovie(Movie movie) {
        dislikedMovies.add(movie);
        shownMovies.add(movie);
        showNextMovieInView();
	}

	private void showNextMovieInView() {
		preparedMoviesCount--;
		movieView.showNextMovie();
		getMovieSuggestionsLazy();
	}

	/**
	 * Loads movies only if there isn't enough of them
     */
    private void getMovieSuggestionsLazy() {
        if (moreMoviesToViewAreNeeded()) {
            tasteKidApi.loadRecommendations(getNextMovieToRetrieveRecommendations().name, LIMIT_OF_SUGGESTIONS) // load recommendations
                    .map(tasteKidResponse -> tasteKidResponse.Similar.Results)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
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
                    .limit(LIMIT_OF_SUGGESTIONS) // enough is enough
                    .filter(movie -> !shownMovies.contains(movie))
                    .subscribe(this::movieRecommendation);
        }
    }

    private Long parseImdbId(final String imdbID) {
        final String substring = imdbID.substring(2);
        return Long.parseLong(substring);
    }

    private boolean moreMoviesToViewAreNeeded() {
        return preparedMoviesCount < MINIMUM_MOVIES_COUNT_THRESHOLD;
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
}
