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
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.network.TasteKidApi;


public class MovieSuggestionEngineInteractor implements IMovieSuggestionEngineInteractor {

	private final TasteKidApi api;


	public MovieSuggestionEngineInteractor(final TasteKidApi tasteKidApi) {
		api = tasteKidApi;
	}

    @Override
	public Observable<Movie> getSuggestion() {
		return api.loadRecommendations("Up!")
				.map(tasteKidResponse -> tasteKidResponse.Similar.Results)
				.flatMap(Observable::from)
				.map(dataItem -> new Movie(1L, dataItem.Name));
	}

    @Override
    public void movieLiked(Movie movie) {

	}

    @Override
    public void movieDisliked(Movie movie) {

    }
}
