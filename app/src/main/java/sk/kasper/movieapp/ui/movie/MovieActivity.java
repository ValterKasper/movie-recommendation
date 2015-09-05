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

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Queue;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sk.kasper.movieapp.R;
import sk.kasper.movieapp.models.Movie;
import sk.kasper.movieapp.ui.BaseActivity;

public class MovieActivity extends BaseActivity implements IMovieView {
    private static final String TAG = "MovieActivity";
    @Bind(R.id.tvMovieName)
    TextView tvMovieName;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private MoviePresenter presenter;

    // TODO make queue
	private Queue<Movie> movieQueue = new ArrayDeque<>();
	private boolean isBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        presenter = new MoviePresenter(this, new MovieSuggestionEngineInteractorMock());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void addMovieCard(Movie movie) {
		movieQueue.add(movie);
	}

	@Override
	public void showNextMovie() {
		movieQueue.remove();
		tvMovieName.setText(movieQueue.element().name);
	}

	@OnClick(R.id.bLike)
	public void likeClick() {
		presenter.onLikeMovie(movieQueue.element());
	}

    @OnClick(R.id.bDislike)
    public void dislikeClick() {
		presenter.onDislikeMovie(movieQueue.element());
	}
}
