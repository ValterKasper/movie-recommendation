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

package sk.kasper.movieapp.ui.movie;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import sk.kasper.movieapp.R;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.ui.BaseActivity;

public class MovieActivity extends BaseActivity implements IMovieView {
    private static final String TAG = "MovieActivity";
    @Bind(R.id.tvMovieName)
    TextView tvMovieName;
    @Bind(R.id.ivCover)
    ImageView ivCover;
    @Bind(R.id.tvImdb)
    TextView tvImdb;
    @Bind(R.id.tvMetascore)
    TextView tvMetascore;
    @Bind(R.id.tvPLot)
    TextView tvPlot;
    @Bind(R.id.bLike)
    Button bLike;
    @Bind(R.id.bDislike)
    Button bDislike;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.tvGenre)
    TextView tvGenre;
    @Bind(R.id.tvDirector)
    TextView tvDirector;
    @Bind(R.id.tvActors)
    TextView tvActors;
    @Bind(R.id.tvCountry)
    TextView tvCountry;
	@Bind(R.id.fabBookmark)
	FloatingActionButton fabBookmark;
	@Bind(R.id.rlCoverBackground)
	RelativeLayout rlCoverBackground;

    private Movie shownMovie;
    private boolean bookmarked;

	@Inject
	MoviePresenter presenter;

	IUserInteraction iUserInteraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

		iUserInteraction = presenter;

		bLike.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				iUserInteraction.onLikeMovie(shownMovie);}
		});

		bDislike.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				iUserInteraction.onDislikeMovie(shownMovie);}
		});

		fabBookmark.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				iUserInteraction.onToogleBookmark(shownMovie);}
		});
	}

    @Override
    public int getDrawerId() {
        return MOVIE_ACTIVITY;
    }

    @Override
    protected void onResume() {
        super.onResume();
		presenter.onResume(this);
	}

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
	public void showNextMovie(final Movie movie) {
		bookmarked = false;
        showAsBookmarked(false);
		shownMovie = movie;
		collapsingToolbarLayout.setTitle(shownMovie.name);
		tvMovieName.setText(shownMovie.name);
		tvGenre.setText(shownMovie.genre);
		tvPlot.setText(shownMovie.plot);
		tvImdb.setText(shownMovie.imdbScore);
		tvMetascore.setText(shownMovie.metascore);
		tvActors.setText(shownMovie.actors);
		tvDirector.setText(shownMovie.director);
		tvCountry.setText(shownMovie.country);

		if (ivCover.getWidth() > 0 && ivCover.getHeight() > 0) {
			showCover();
		} else { // sometimes called from onResume
			ivCover.getViewTreeObserver()
					.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
						// Wait until layout to call Picasso
						@Override
						public void onGlobalLayout() {
							// Ensure we call this only once
							ivCover.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);

							showCover();
						}
					});
		}

	}

	public void showAsBookmarked(final boolean bookmarked) {
        this.bookmarked = bookmarked;
        fabBookmark.setImageResource(bookmarked ? R.drawable.ic_bookmark_remove : R.drawable.ic_bookmark_add);
    }

	@Override
	public void showErrorMessage(final String error) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(error)
				.setTitle("Taste Kid Api error");
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void showCover() {
		Picasso.with(MovieActivity.this)
				.load(shownMovie.coverUrl)
				.into(ivCover, new Callback.EmptyCallback() {
					@Override
					public void onSuccess() {
						Bitmap bitmap = ((BitmapDrawable) ivCover.getDrawable()).getBitmap();

						// Asynchronous
						Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
							@Override
							public void onGenerated(final Palette p) {
								final int darkMutedColor = p.getDarkVibrantColor(0x000000);
								rlCoverBackground.setBackgroundColor(darkMutedColor);
							}
						});
					}
				});
	}
}
