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

package sk.kasper.movieapp.ui;

import android.content.Intent;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sk.kasper.movieapp.R;
import sk.kasper.movieapp.ui.bookmarks.BookmarksListActivity;
import sk.kasper.movieapp.ui.info.InfoActivity;
import sk.kasper.movieapp.ui.movie.MovieActivity;

/**
 * Just works with drawer
 */
public class DrawerHelper {
    @Bind(R.id.bookmarksDrawerItem)
    Button bookmarksDrawerItem;
    @Bind(R.id.moviesDrawerItem)
    Button moviesDrawerItem;
	@Bind(R.id.infoDrawerItem)
	Button infoDrawerItem;

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
			case (BaseActivity.INFO_ACTIVITY):
				infoDrawerItem.setEnabled(false);
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

	@OnClick(R.id.infoDrawerItem)
	public void showInfoClick() {
		Intent intent = new Intent(activity, InfoActivity.class);
		activity.startActivity(intent);
    }
}
