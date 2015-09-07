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

import sk.kasper.movieapp.models.Movie;

/**
 * Manipulates with UI and interactor
 */
public class MoviePresenter {

    private static final int MINIMUM_MOVIES_COUNT_THRESHOLD = 3;
    private IMovieSuggestionEngineInteractor movieInteractor;
    private IMovieView movieView;

    /**
     * Count of movies prepared to be shown in view
     */
    private int preparedMoviesCount = 0;

    public MoviePresenter(IMovieView movieView, IMovieSuggestionEngineInteractor movieInteractor) {
        this.movieView = movieView;
        this.movieInteractor = movieInteractor;

        movieView.getMovieLikeStream().subscribe(like -> {
            onLikeMovie(like.getMovie());
        });

        movieView.getMovieDislikeStream().subscribe(dislike -> {
            onDislikeMovie(dislike.getMovie());
        });
    }

    /**
     * Loads movie suggestions from interactor and sends them to view
     */
    private void subscribeSuggestionStream() {
        movieInteractor.getSuggestionStream()
                .subscribe((movie) -> {
                    preparedMoviesCount++;
                    movieView.addMovieCard(movie);
                });
    }

    public void onResume() {
        subscribeSuggestionStream();
    }

    private void onLikeMovie(Movie movie) {
        preparedMoviesCount--;
        getMovieSuggestionsLazy();
        movieInteractor.movieLiked(movie);
        movieView.showNextMovie();
    }

    private void onDislikeMovie(Movie movie) {
        preparedMoviesCount--;
        getMovieSuggestionsLazy();
        movieInteractor.movieDisliked(movie);
        movieView.showNextMovie();
    }

    /**
     * Loads movies only if there isn't enough of them
     */
    private void getMovieSuggestionsLazy() {
        if (moreMoviesToViewAreNeeded()) {
            subscribeSuggestionStream();
        }
    }

    private boolean moreMoviesToViewAreNeeded() {
        return preparedMoviesCount < MINIMUM_MOVIES_COUNT_THRESHOLD;
    }
}
