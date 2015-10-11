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

import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import rx.Observable;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.network.OmdbApi;
import sk.kasper.movieapp.network.TasteKidApi;


public class MovieSuggestionEngineInteractor implements IMovieSuggestionEngineInteractor {

	private static final int LIMIT_OF_SUGGESTIONS = 3;
	private static final Movie[] SEED_MOVIES = {new Movie(1L, "Up!"), new Movie(2L, "Matrix"), new Movie(3L, "Rush"), new Movie(4L, "Everest")};
	private int seedMoviesIndex = 0;
	private final TasteKidApi tasteKidApi;
	private OmdbApi omdbApi;
	private List<Movie> shownMovies = new ArrayList<>();
	private List<Movie> dislikedMovies = new ArrayList<>();
	private Queue<Movie> likedMoviesQueue = new ArrayDeque<>();

	public MovieSuggestionEngineInteractor(final TasteKidApi tasteKidApi, final OmdbApi omdbApi) {
		this.tasteKidApi = tasteKidApi;
		this.omdbApi = omdbApi;
	}

	private boolean isMoreMoviesToRetrieveRecommendations() {return true;}

	// todo extract to class
	private Movie getNextMovieToRetrieveRecommendations() {
		if (likedMoviesQueue.isEmpty()) {
			return SEED_MOVIES[seedMoviesIndex++];
		} else {
			return likedMoviesQueue.remove();
		}
	}

    @Override
	public Observable<Movie> getSuggestionStream() {
		return loadMovieSuggestions();
	}

	@Override
	public void movieLiked(Movie movie) {
		likedMoviesQueue.add(movie);
		shownMovies.add(movie);
	}

    @Override
    public void movieDisliked(Movie movie) {
		dislikedMovies.add(movie);
		shownMovies.add(movie);
	}

	@NonNull
	private Observable<Movie> loadMovieSuggestions() {
		if (isMoreMoviesToRetrieveRecommendations()) {
			return tasteKidApi.loadRecommendations(getNextMovieToRetrieveRecommendations().name, LIMIT_OF_SUGGESTIONS) // load recommendations
					.map(tasteKidResponse -> tasteKidResponse.Similar.Results)
					.flatMap(Observable::from)
					.flatMap(tasteKidRespItem -> omdbApi.getDetailOfMovie(tasteKidRespItem.Name)) // get detail of movie
					.flatMap(omdbResp -> Observable.just(new Movie( // create movie stream
							parseImdbId(omdbResp.imdbID),
							omdbResp.Title,
							omdbResp.Poster,
							omdbResp.Plot,
							omdbResp.imdbRating,
							omdbResp.Metascore)))
					.filter(movie -> !shownMovies.contains(movie)) // movie cant be shown again
					.limit(LIMIT_OF_SUGGESTIONS); // enough is enough
		} else {
			return Observable.error(new IllegalStateException("No liked movies for creating recommendations"));
		}
	}

	private Long parseImdbId(final String imdbID) {
		final String substring = imdbID.substring(2);
		return Long.parseLong(substring);
	}
}
