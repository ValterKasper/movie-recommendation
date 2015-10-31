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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import sk.kasper.movieapp.R;
import sk.kasper.movieapp.Utils;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.storage.BookmarksStorage;
import sk.kasper.movieapp.ui.BaseActivity;

public class BookmarksListActivity
		extends BaseActivity {

	@Bind(R.id.lvBookmarks)
	ListView lvBookmarks;

    BookmarksStorage bookmarksStorage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmarks_list);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		ButterKnife.bind(this);

        bookmarksStorage = new BookmarksStorage(Utils.getSharedPrefs(this));
        lvBookmarks.setAdapter(new BookmarksAdapter(bookmarksStorage.loadBookmarks(), this));
		registerForContextMenu(lvBookmarks);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenu.ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.lvBookmarks) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(bookmarksStorage.loadBookmarks().get(info.position).name);
			menu.add("Remove from bookmarks");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		bookmarkRemove(info.position);
		return true;
	}

    @Override
    public int getDrawerId() {
        return BOOKMARKS_ACTIVITY;
    }

	public boolean bookmarkRemove(int position) {
		Movie movie = (Movie) lvBookmarks.getAdapter().getItem(position);
		movie.bookmarked = false;
        bookmarksStorage.bookmarkMovie(movie);
        lvBookmarks.setAdapter(new BookmarksAdapter(bookmarksStorage.loadBookmarks(), this));

		return true;
	}

    public static class BookmarksAdapter
            extends BaseAdapter {

		private final List<Movie> bookmarks;
		private final Context context;
		private final LayoutInflater inflater;

		private BookmarksAdapter(final List<Movie> bookmarks, final Context context) {
			this.bookmarks = bookmarks;
			this.context = context;
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
			holder.tvType.setText(movie.genre);
			Picasso
					.with(context)
					.load(movie.coverUrl)
					.into(holder.ivCover);

			return convertView;
		}

		public static class ViewHolder {

			@Bind(R.id.tvTitle)
			public TextView tvTitle;
			@Bind(R.id.tvType)
			public TextView tvType;
			@Bind(R.id.ivCover)
			public ImageView ivCover;

			public ViewHolder(final View convertView) {
				ButterKnife.bind(this, convertView);
			}
		}
	}
}
