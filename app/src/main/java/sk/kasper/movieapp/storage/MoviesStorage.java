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

package sk.kasper.movieapp.storage;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import sk.kasper.movieapp.models.Movie;

/**
 * Storage for users movies
 */
public class MoviesStorage {

	private static final String PREF_SHOWN_MOVIES = "pref-shown-movie";
	private static final String PREF_DISLIKED_MOVIES = "pref-disliked-movie";
	private static final String PREF_LIKED_MOVIES = "pref-liked-movie";
	private static final String PREF_MOVIES_TO_BE_SHOWN = "pref-movies-to-be-shown";

	@Inject
	SharedPreferences sharedPref;
	private final Gson gson = new Gson();
	;

	public void saveLikedMovies(final List<Movie> movies) {
		saveMovies(movies, PREF_LIKED_MOVIES);
	}

	public void saveDislikedMovies(final List<Movie> movies) {
		saveMovies(movies, PREF_DISLIKED_MOVIES);
	}

	public void saveShownMovies(final List<Movie> movies) {
		saveMovies(movies, PREF_SHOWN_MOVIES);
	}

	public void saveMoviesToBeShown(final List<Movie> movies) {
		saveMovies(movies, PREF_MOVIES_TO_BE_SHOWN);
	}

	public List<Movie> loadShownMovies() {
		return loadMovies(PREF_SHOWN_MOVIES);
	}

	public List<Movie> loadLikedMovies() {
		return loadMovies(PREF_LIKED_MOVIES);
	}

	public List<Movie> loadDislikedMovies() {
		return loadMovies(PREF_DISLIKED_MOVIES);
	}

	public List<Movie> loadMoviesToBeShown() {
		return loadMovies(PREF_MOVIES_TO_BE_SHOWN);
	}

	private List<Movie> loadMovies(final String prefKey) {
		Type collectionType = new TypeToken<List<Movie>>() {
		}.getType();
		return gson.fromJson(sharedPref.getString(prefKey, "[]"), collectionType);
	}

	private void saveMovies(final List<Movie> movies, String prefKey) {
		sharedPref.edit().putString(prefKey, gson.toJson(movies)).apply();
	}
}
