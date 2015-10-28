package sk.kasper.movieapp.ui;

import android.content.Intent;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sk.kasper.movieapp.R;
import sk.kasper.movieapp.ui.bookmarks.BookmarksListActivity;
import sk.kasper.movieapp.ui.movie.MovieActivity;

/**
 * Just works with drawer
 */
public class DrawerHelper {
    @Bind(R.id.bookmarksDrawerItem)
    Button bookmarksDrawerItem;
    @Bind(R.id.moviesDrawerItem)
    Button moviesDrawerItem;

    private BaseActivity activity;

    public DrawerHelper(BaseActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        switch (activity.getDrawerId()) {
            case (BaseActivity.MOVIE_ACTIVITY):
                moviesDrawerItem.setEnabled(false);
                break;
            case (BaseActivity.BOOKMARKS_ACTIVITY):
                bookmarksDrawerItem.setEnabled(false);
                break;
        }
    }

    @OnClick(R.id.moviesDrawerItem)
    public void showMoviesClick() {
        Intent intent = new Intent(activity, MovieActivity.class);
        activity.startActivity(intent);
    }

    @OnClick(R.id.bookmarksDrawerItem)
    public void showBookmarksClick() {
        Intent intent = new Intent(activity, BookmarksListActivity.class);
        activity.startActivity(intent);
    }
}
