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

package sk.kasper.movieapp;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.ui.events.ApiErrorEvent;
import sk.kasper.movieapp.ui.movie.MovieService;

/**
 * Used aplication
 */
public class MovieApplication extends Application {
    private Bus bus;
    private MovieService movieService;

	@Override
	public void onCreate() {
		super.onCreate();
        bus = new Bus(ThreadEnforcer.MAIN);
        movieService = new MovieService(Utils.getTasteKidApi(), Utils.getOmdbApi(), bus);
        bus.register(movieService);
        bus.register(this);
    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {
        Toast.makeText(MovieApplication.this, event.msg, Toast.LENGTH_SHORT).show();
        Log.e("ReaderApp", event.msg);
    }

    public Bus getBus() {
        return bus;
    }

    @Subscribe
    public void movieRecommendation(Movie movie) {
        Log.d("Tralala", "movieRecommendation ");
    }
}
