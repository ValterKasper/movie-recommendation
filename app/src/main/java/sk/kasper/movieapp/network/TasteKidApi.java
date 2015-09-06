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

package sk.kasper.movieapp.network;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.models.TasteKidResponse;

/**
 * Api for movie REST services
 */
public interface TasteKidApi {

	String REST_TASTEKID_ENDPOINT = "https://www.tastekid.com/api/";
	String REST_APIARY_ENDPOINT = "http://private-e16bf-nottoday.apiary-mock.com";

	/**
	 * Retrieves similar movies to given movie name
	 *
	 * @param movieName - favorite movie
	 *
	 * @return response from taste kid server
	 */
	@GET("/similar?type=movies")
	Observable<TasteKidResponse> loadMovies(@Query("q") String movieName);

	/**
	 * Temporary
	 * Just for starting
	 */
	@GET("/movies")
	Observable<List<Movie>> loadMovies();

	/**
	 * Temporary
	 * Just for starting
	 */
	@GET("/movie")
	Observable<Movie> loadMovie();
}
