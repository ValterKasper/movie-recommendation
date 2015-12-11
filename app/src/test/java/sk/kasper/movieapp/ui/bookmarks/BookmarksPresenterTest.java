package sk.kasper.movieapp.ui.bookmarks;

import com.google.common.collect.Lists;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.storage.BookmarksStorage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class BookmarksPresenterTest {

    @Mock
    BookmarksContract.BookmarksView view;

    @Mock
    BookmarksStorage bookmarksStorage;

    BookmarksPresenter presenter;

    public static final List<Movie> MOVIES = Lists.newArrayList(new Movie(10L, "Nah"));

    private static final Movie BOOKMARKED_MOVIE = new Movie(11L, "Meh");

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        presenter = new BookmarksPresenter(bookmarksStorage);
        presenter.onResume(view);
    }

    @Test
    public void testLoadBookmarks() throws Exception {
        when(bookmarksStorage.loadBookmarks()).thenReturn(MOVIES);
        presenter.loadBookmarks();

        verify(bookmarksStorage).loadBookmarks();
        verify(view).showBookmarks(MOVIES);
    }

    @Test
    public void testBookmarkMovie() throws Exception {
        presenter.bookmarkMovie(BOOKMARKED_MOVIE);
        verify(bookmarksStorage).bookmarkMovie(BOOKMARKED_MOVIE);
    }
}