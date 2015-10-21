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
import com.squareup.otto.Subscribe;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.ui.events.LoadSuggestionsEvent;

/**
 * Manipulates with UI
 */
public class MoviePresenter {

    private static final int MINIMUM_MOVIES_COUNT_THRESHOLD = 3;
    private static final int LIMIT_OF_SUGGESTIONS = 3;
    private static final Movie[] SEED_MOVIES = {new Movie(1L, "Up!"), new Movie(2L, "Matrix"), new Movie(3L, "Rush"), new Movie(4L, "Everest")};
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

    public MoviePresenter(IMovieView movieView, Bus bus) {
        this.movieView = movieView;
        this.bus = bus;

        movieView.getMovieLikeStream().subscribe(like -> {
            onLikeMovie(like.getMovie());
        });

        movieView.getMovieDislikeStream().subscribe(dislike -> {
            onDislikeMovie(dislike.getMovie());
        });
    }

    public void onResume() {
        bus.register(this);
        getMovieSuggestionsLazy();
        bus.post(new String("ABC"));
    }

    @Subscribe
    public void recieveLong(Long l) {
        Log.d("asd", "recieveLong " + l);
    }

    @Subscribe
    public void movieRecommendation(Movie movie) {
        if (!shownMovies.contains(movie)) {
            preparedMoviesCount++;
            movieView.addMovieCard(movie);

            if (noMovieIsShown) {
                Log.d("FOO", "subscribeSuggestionStream no movie shown");
                showNextMovieInView();
                noMovieIsShown = false;
            }
        }
    }

    public void onPause() {
        bus.unregister(this);
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
            bus.post(new LoadSuggestionsEvent(getNextMovieToRetrieveRecommendations()));
        }
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
