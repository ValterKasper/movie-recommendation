package sk.kasper.movieapp.models;

/**
 * Represents Like of movie
 */
public class MovieLike {
    private Movie movie;

    public MovieLike(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }
}
