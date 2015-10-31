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

import rx.Observable;
import sk.kasper.movieapp.models.BookmarkToggle;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.models.MovieDislike;
import sk.kasper.movieapp.models.MovieLike;

/**
 * Represents UI for movie suggestion.
 */
public interface IMovieView {

	/**
	 * @return Infinite stream of likes. Yap!
	 */
	Observable<MovieLike> getMovieLikeStream();
	/**
	 * @return Stream of dislikes
	 */
	Observable<MovieDislike> getMovieDislikeStream();
	/**
	 * @return Stream of bookmark clicks
	 */
	Observable<BookmarkToggle> getMovieBookmarkToggleStream();
	void showProgressBar();
	void hideProgressBar();

	/**
	 * Show next movie that was added with addMovieCard
	 */
	void showNextMovie(final Movie movie);

	/**
	 * Display current movie as bookmarked
	 *
	 * @param bookmarked is movie (yoda time)
	 */
	void showAsBookmarked(final boolean bookmarked);

	/**
	 * Show error somehow
	 *
	 * @param error to be shown
	 */
	void showErrorMessage(String error);
}
