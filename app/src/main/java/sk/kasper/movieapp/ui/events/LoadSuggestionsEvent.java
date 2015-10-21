package sk.kasper.movieapp.ui.events;

import sk.kasper.movieapp.models.Movie;

/**
 * Created by Valter on 21.10.2015.
 */
public class LoadSuggestionsEvent {
    public final Movie movie;

    public LoadSuggestionsEvent(Movie movie) {
        this.movie = movie;
    }
}
