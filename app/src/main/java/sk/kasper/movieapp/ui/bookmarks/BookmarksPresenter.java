package sk.kasper.movieapp.ui.bookmarks;

import javax.inject.Inject;

import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.storage.BookmarksStorage;


public class BookmarksPresenter implements BookmarksContract.UserInteraction {
    private BookmarksContract.BookmarksView view;

    @Inject
    BookmarksStorage bookmarksStorage;

    public void onResume(BookmarksContract.BookmarksView view) {
        this.view = view;
    }

    @Override
    public void loadBookmarks() {
        view.showBookmarks(bookmarksStorage.loadBookmarks());
    }

    @Override
    public void bookmarkMovie(Movie movie) {
        bookmarksStorage.bookmarkMovie(movie);
    }
}
