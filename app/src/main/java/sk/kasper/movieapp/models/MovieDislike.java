package sk.kasper.movieapp.models;

/**
 * Dislike of movie
 */
public class MovieDislike {
    private Movie movie;

    public MovieDislike(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }
}
