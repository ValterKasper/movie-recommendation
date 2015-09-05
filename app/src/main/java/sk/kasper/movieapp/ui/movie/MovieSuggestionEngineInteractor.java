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

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.network.ApiaryApi;


public class MovieSuggestionEngineInteractor implements IMovieSuggestionEngineInteractor {

	private static Subscriber<? super Movie> subscriber;
	private final ApiaryApi api;


	public MovieSuggestionEngineInteractor(final ApiaryApi apiaryApi) {
		api = apiaryApi;
	}

    @Override
	public Observable<Movie> getSuggestion() {
		api.loadMovies().subscribe(new Action1<List<Movie>>() {
			@Override
			public void call(final List<Movie> movies) {
				for (final Movie movie : movies) {
					subscriber.onNext(movie);
				}
			}
		});

		return Observable.create(new Observable.OnSubscribe<Movie>() {

			@Override
			public void call(final Subscriber<? super Movie> subscriber) {
				MovieSuggestionEngineInteractor.subscriber = subscriber;
			}
		});
	}

    @Override
    public void movieLiked(Movie movie) {
		api.loadMovie().subscribe(new Action1<Movie>() {
			@Override
			public void call(final Movie movie) {
				subscriber.onNext(movie);
			}
		});
	}

    @Override
    public void movieDisliked(Movie movie) {

    }
}
