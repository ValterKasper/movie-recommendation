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

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import sk.kasper.movieapp.models.OmdbResponse;

/**
 * Api to The Open Movie Database
 */
public interface OmdbApi {

	String REST_OMDB_ENDPOINT = "http://www.omdbapi.com";
	String REST_APIARY_ENDPOINT = "http://private-e16bf-nottoday.apiary-mock.com/detail";

	@GET("/?plot=full&r=json")
	Observable<OmdbResponse> getDetailOfMovie(@Query("t") String movieTitle);
}
