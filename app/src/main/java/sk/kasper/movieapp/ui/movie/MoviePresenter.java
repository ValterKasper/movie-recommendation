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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import sk.kasper.movieapp.exceptions.TasteKidResponseException;
import sk.kasper.movieapp.models.BookmarkToggle;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.models.MovieDislike;
import sk.kasper.movieapp.models.MovieLike;
import sk.kasper.movieapp.models.OmdbResponse;
import sk.kasper.movieapp.models.TasteKidResponse;
import sk.kasper.movieapp.network.OmdbApi;
import sk.kasper.movieapp.network.TasteKidApi;
import sk.kasper.movieapp.network.TasteKidApiKey;
import sk.kasper.movieapp.storage.BookmarksStorage;
import sk.kasper.movieapp.storage.MoviesStorage;

/**
 * Manipulates with UI
 */
public class MoviePresenter {

	private static final int MINIMUM_MOVIES_COUNT_THRESHOLD = 3;
	private static final int LIMIT_OF_SUGGESTIONS = 8;
	private static final Movie[] SEED_MOVIES = {
			new Movie(1L, "Pulp Fiction"),
			new Movie(40L, "Toy Story"),
			new Movie(2L, "Matrix"),
			new Movie(3L, "Rush"),
			new Movie(4L, "Forrest Gump"),
			new Movie(41L, "Rocky"),
			new Movie(5L, "Schindler's List"),
			new Movie(6L, "Godfather, The"),
			new Movie(7L, "Up!")
	};
	private static final String TAG = "MoviePresenter";

	@Inject
	TasteKidApi tasteKidApi;
	@Inject
	OmdbApi omdbApi;
	@Inject
	BookmarksStorage bookmarksStorage;
	@Inject
	MoviesStorage moviesStorage;
	@Inject
	TasteKidApiKey tastekidApiKey;
	@Inject
	ImdbIdParser imdbIdParser;
	@Inject
	GoodMovieFinder goodMovieFinder;
	@Inject
	TasteKidResponseProcessor tasteKidResponseProcessor;

	private IMovieView movieView;
	private boolean noMovieIsShown = true;
	private int seedMoviesIndex = 0;
	private List<Movie> shownMovies;
	private List<Movie> dislikedMovies;
	private List<Movie> likedMovies;

	/**
	 * Movies prepared to be shown
	 */
	private List<Movie> movieToBeShown;


	private boolean isMoreMoviesToRetrieveRecommendations() {
		return true;
	}

	private Movie getNextMovieToRetrieveRecommendations() {
		if (likedMovies.isEmpty()) {
			return SEED_MOVIES[seedMoviesIndex++];
		} else {
			return likedMovies.remove(likedMovies.size() - 1);
		}
	}

	public void onResume(final IMovieView movieView) {
		this.movieView = movieView;

		this.shownMovies = moviesStorage.loadShownMovies();
		this.likedMovies = moviesStorage.loadLikedMovies();
		this.dislikedMovies = moviesStorage.loadDislikedMovies();
		this.movieToBeShown = moviesStorage.loadMoviesToBeShown();

		movieView.getMovieLikeStream().subscribe(new Action1<MovieLike>() {
			@Override
			public void call(final MovieLike like) {
				MoviePresenter.this.onLikeMovie(like.getMovie());
			}
		});

		movieView.getMovieDislikeStream().subscribe(new Action1<MovieDislike>() {
			@Override
			public void call(final MovieDislike dislike) {
				MoviePresenter.this.onDislikeMovie(dislike.getMovie());
			}
		});

		movieView.getMovieBookmarkToggleStream().subscribe(new Action1<BookmarkToggle>() {
			@Override
			public void call(final BookmarkToggle bookmarkToggle) {
				MoviePresenter.this.onBookmarkMovie(bookmarkToggle.movie);
			}
		});

		if (moreMoviesToViewAreNeeded()) {
			getMovieSuggestions();
		} else {
			showNextMovieInView();
		}
	}

	public void movieRecommendation(Movie movie) {
		movieToBeShown.add(movie);

		if (noMovieIsShown) {
			Log.d("FOO", "subscribeSuggestionStream no movie shown");
			showNextMovieInView();
		}
	}

	public void onPause() {
		moviesStorage.saveLikedMovies(new ArrayList<>(likedMovies));
		moviesStorage.saveDislikedMovies(dislikedMovies);
		moviesStorage.saveShownMovies(shownMovies);
		moviesStorage.saveMoviesToBeShown(new ArrayList<>(movieToBeShown));
	}

	private void onLikeMovie(Movie movie) {
		likedMovies.add(movie);
		shownMovies.add(movie);
		movieToBeShown.remove(0);
		showNextMovieInView();
	}

	private void onDislikeMovie(Movie movie) {
		dislikedMovies.add(movie);
		shownMovies.add(movie);
		movieToBeShown.remove(0);
		showNextMovieInView();
	}

	private void onBookmarkMovie(final Movie movie) {
		movie.bookmarked = !movie.bookmarked;
		movieView.showAsBookmarked(movie.bookmarked);
		bookmarksStorage.bookmarkMovie(movie);
	}

	private void showNextMovieInView() {
		movieView.showNextMovie(movieToBeShown.get(0));
		noMovieIsShown = false;

		if (moreMoviesToViewAreNeeded()) {
			getMovieSuggestions();
		}
	}

	/**
	 * Loads movies only if there isn't enough of them
	 */
	private void getMovieSuggestions() {
		tasteKidApi.loadRecommendations(getNextMovieToRetrieveRecommendations().name, LIMIT_OF_SUGGESTIONS, tastekidApiKey.getValue()) // load recommendations
				.map(new Func1<TasteKidResponse, List<TasteKidResponse.Similar.DataItem>>() {
					@Override
					public List<TasteKidResponse.Similar.DataItem> call(final TasteKidResponse tasteKidResponse) {return tasteKidResponseProcessor.processTasteKidResponse(tasteKidResponse);}
				})
				.flatMap(new Func1<List<TasteKidResponse.Similar.DataItem>, Observable<? extends TasteKidResponse.Similar.DataItem>>() {
					@Override
					public Observable<? extends TasteKidResponse.Similar.DataItem> call(final List<TasteKidResponse.Similar.DataItem> iterable) {return Observable.from(iterable);}
				})
				.flatMap(new Func1<TasteKidResponse.Similar.DataItem, Observable<? extends OmdbResponse>>() {
					@Override
					public Observable<? extends OmdbResponse> call(final TasteKidResponse.Similar.DataItem tasteKidRespItem) {return omdbApi.getDetailOfMovie(tasteKidRespItem.Name);}
				}) // get detail of movie
				.flatMap(new Func1<OmdbResponse, Observable<? extends Movie>>() {
					@Override
					public Observable<? extends Movie> call(final OmdbResponse omdbResp) {
						return Observable.just(new Movie( // create movie stream
								imdbIdParser.parseImdbId(omdbResp.imdbID),
								omdbResp.Title,
								omdbResp.Poster,
								omdbResp.Plot,
								omdbResp.imdbRating,
								omdbResp.Metascore,
								omdbResp.Genre,
								omdbResp.Actors,
								omdbResp.Director,
								omdbResp.Country));
					}
				})
				.filter(new Func1<Movie, Boolean>() {
					@Override
					public Boolean call(final Movie movie1) {return goodMovieFinder.hasGoodRating(movie1);}
				})
				.filter(new Func1<Movie, Boolean>() {
					@Override
					public Boolean call(final Movie movie) {return !shownMovies.contains(movie);}
				})
				.limit(LIMIT_OF_SUGGESTIONS) // enough is enough
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.newThread())
				.subscribe(new Observer<Movie>() {
					@Override
					public void onCompleted() {


					}

					@Override
					public void onError(final Throwable e) {
						if (e instanceof TasteKidResponseException) {
							movieView.showErrorMessage(((TasteKidResponseException) e).error);
						}
						Log.d(TAG, "exception", e);
					}

					@Override
					public void onNext(final Movie movie) {
						movieRecommendation(movie);
					}
				});
	}

	private boolean moreMoviesToViewAreNeeded() {
		return movieToBeShown.size() < MINIMUM_MOVIES_COUNT_THRESHOLD;
	}
}
