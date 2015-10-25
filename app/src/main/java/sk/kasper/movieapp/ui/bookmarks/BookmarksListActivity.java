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

package sk.kasper.movieapp.ui.bookmarks;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemLongClick;
import sk.kasper.movieapp.R;
import sk.kasper.movieapp.events.BookmarkMovieEventRequest;
import sk.kasper.movieapp.events.BookmarkMovieEventResponse;
import sk.kasper.movieapp.events.LoadBookmarksEventRequest;
import sk.kasper.movieapp.events.LoadBookmarksEventResponse;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.ui.BaseActivity;

public class BookmarksListActivity
		extends BaseActivity {

	@Bind(R.id.lvBookmarks)
	ListView lvBookmarks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmarks_list);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		ButterKnife.bind(this);

		bus.register(this);
		bus.post(new LoadBookmarksEventRequest());
	}

	@OnItemLongClick(R.id.lvBookmarks)
	public boolean bookmarkLongClick(int position) {
		Movie movie = (Movie) lvBookmarks.getAdapter().getItem(position);
		movie.bookmarked = false;
		bus.post(new BookmarkMovieEventRequest(movie));

		return true;
	}

	@Subscribe
	public void bookmarkEventResponse(BookmarkMovieEventResponse event) {
		bus.post(new LoadBookmarksEventRequest());
	}

	@Subscribe
	public void loadBookmarsRespose(LoadBookmarksEventResponse event) {
		lvBookmarks.setAdapter(new BookmarksAdapter(event.bookmarks, this));
	}

	public static class BookmarksAdapter
			extends BaseAdapter {

		private final List<Movie> bookmarks;
		private final LayoutInflater inflater;

		private BookmarksAdapter(final List<Movie> bookmarks, final Context context) {
			this.bookmarks = bookmarks;
			this.inflater = ((Activity) context).getLayoutInflater();
		}

		@Override
		public int getCount() {
			return bookmarks.size();
		}

		@Override
		public Object getItem(final int i) {
			return bookmarks.get(i);
		}

		@Override
		public long getItemId(final int i) {
			return 0;
		}

		@Override
		public View getView(final int i, View convertView, final ViewGroup parentView) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.bookmark_list_item, parentView, false);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Movie movie = (Movie) getItem(i);
			holder.tvTitle.setText(movie.name);

			return convertView;
		}

		public static class ViewHolder {

			@Bind(R.id.tvTitle)
			public TextView tvTitle;

			public ViewHolder(final View convertView) {
				ButterKnife.bind(this, convertView);
			}
		}
	}
}
