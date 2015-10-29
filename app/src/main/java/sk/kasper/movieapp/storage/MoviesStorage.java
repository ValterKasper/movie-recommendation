package sk.kasper.movieapp.storage;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import sk.kasper.movieapp.models.Movie;

/**
 * Storage for users movies
 */
public class MoviesStorage {
    private static final String PREF_SHOWN_MOVIES = "pref-shown-movie";
    private static final String PREF_DISLIKED_MOVIES = "pref-disliked-movie";
    private static final String PREF_LIKED_MOVIES = "pref-liked-movie";
    private final SharedPreferences sharedPref;
    private final Gson gson;

    public MoviesStorage(SharedPreferences sharedPref) {
        this.sharedPref = sharedPref;
        gson = new Gson();
    }

    public void saveLikedMovies(final List<Movie> movies) {
        saveMovies(movies, PREF_LIKED_MOVIES);
    }

    public void saveDislikedMovies(final List<Movie> movies) {
        saveMovies(movies, PREF_DISLIKED_MOVIES);
    }

    public void saveShownMovies(final List<Movie> movies) {
        saveMovies(movies, PREF_SHOWN_MOVIES);
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

    private List<Movie> loadMovies(final String prefKey) {
        Type collectionType = new TypeToken<List<Movie>>() {
        }.getType();
        return gson.fromJson(sharedPref.getString(prefKey, "[]"), collectionType);
    }

    private void saveMovies(final List<Movie> movies, String prefKey) {
        sharedPref.edit().putString(prefKey, gson.toJson(movies)).apply();
    }
}
