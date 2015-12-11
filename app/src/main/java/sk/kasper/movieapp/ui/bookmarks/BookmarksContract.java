package sk.kasper.movieapp.ui.bookmarks;


import java.util.List;

import sk.kasper.movieapp.models.Movie;

public interface BookmarksContract {

    interface BookmarksView {
        void showBookmarks(final List<Movie> movies);
    }

    interface  UserInteraction {
        void loadBookmarks();
        void onResume(BookmarksContract.BookmarksView view);
        void bookmarkMovie(Movie movie);
    }
}
