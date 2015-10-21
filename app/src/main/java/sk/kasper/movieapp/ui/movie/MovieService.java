package sk.kasper.movieapp.ui.movie;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.network.OmdbApi;
import sk.kasper.movieapp.network.TasteKidApi;
import sk.kasper.movieapp.ui.events.LoadSuggestionsEvent;


/**
 * Created by Valter on 21.10.2015.
 */
public class MovieService {
    private static final int LIMIT_OF_SUGGESTIONS = 3;
    private final TasteKidApi tasteKidApi;
    private final OmdbApi omdbApi;
    private final Bus bus;

    public MovieService(TasteKidApi tasteKidApi, OmdbApi omdbApi, Bus bus) {
        this.tasteKidApi = tasteKidApi;
        this.omdbApi = omdbApi;
        this.bus = bus;
    }

    @Subscribe
    public void loadMovieSuggestions(LoadSuggestionsEvent event) {
        tasteKidApi.loadRecommendations(event.movie.name, LIMIT_OF_SUGGESTIONS) // load recommendations
                .map(tasteKidResponse -> tasteKidResponse.Similar.Results)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::from)
                .flatMap(tasteKidRespItem -> omdbApi.getDetailOfMovie(tasteKidRespItem.Name)) // get detail of movie
                .flatMap(omdbResp -> Observable.just(new Movie( // create movie stream
                        parseImdbId(omdbResp.imdbID),
                        omdbResp.Title,
                        omdbResp.Poster,
                        omdbResp.Plot,
                        omdbResp.imdbRating,
                        omdbResp.Metascore,
                        omdbResp.Genre,
                        omdbResp.Actors,
                        omdbResp.Director,
                        omdbResp.Country)))
                .limit(LIMIT_OF_SUGGESTIONS) // enough is enough
                .subscribe(movie -> {
                    bus.post(movie);
                });

        bus.post(new Movie(42L, "asd"));
    }

    private Long parseImdbId(final String imdbID) {
        final String substring = imdbID.substring(2);
        return Long.parseLong(substring);
    }

    @Subscribe
    public void longToString(String event) {
        bus.post(new Long(42));
    }
}
