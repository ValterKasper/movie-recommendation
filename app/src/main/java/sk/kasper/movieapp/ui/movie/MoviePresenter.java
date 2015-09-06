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

import rx.Subscription;
import sk.kasper.movieapp.models.Movie;

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

    private Subscription getMovieSugestion() {
        return movieInteractor.getSuggestion()
                .subscribe(movieView::addMovieCard);
    }

    public void onResume() {
        getMovieSugestion();
    }

    public void onLikeMovie(Movie movie) {
        movieInteractor.movieLiked(movie);
        //getMovieSugestion();
        movieView.showNextMovie();
    }

    public void onDislikeMovie(Movie movie) {
        movieInteractor.movieDisliked(movie);
        //getMovieSugestion();
        movieView.showNextMovie();
    }
}
