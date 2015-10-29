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

import sk.kasper.movieapp.models.Movie;


/**
 * Storage for bookmarks
 */
public class BookmarksStorage {
	private static final String TAG = "BookmarksStorage";
	private static final String PREF_BOOKMARKS = "pref-bookmarks";
	private final SharedPreferences sharedPref;

	public BookmarksStorage(final SharedPreferences sharedPref) {
		this.sharedPref = sharedPref;
	}

	public void bookmarkMovie(final Movie movie) {
		final List<Movie> movies = loadBookmarks();
		if (movie.bookmarked) {
			movies.add(movie);
		} else {
			movies.remove(movie);
		}

		Gson gson = new Gson();
		sharedPref.edit().putString(PREF_BOOKMARKS, gson.toJson(movies)).apply();
	}

	public List<Movie> loadBookmarks() {
		Type collectionType = new TypeToken<List<Movie>>() {}.getType();
		Gson gson = new Gson();

		return gson.fromJson(sharedPref.getString(PREF_BOOKMARKS, "[]"), collectionType);
	}
}
