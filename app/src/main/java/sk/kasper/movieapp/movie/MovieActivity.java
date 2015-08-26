package sk.kasper.movieapp.movie;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import sk.kasper.movieapp.BaseActivity;
import sk.kasper.movieapp.R;
import sk.kasper.movieapp.services.BindingMovieService;
import sk.kasper.movieapp.services.MovieService;

public class MovieActivity extends BaseActivity implements IMovieView {
    private static final String TAG = "MovieActivity";
    @Bind(R.id.tvMovieName)
    TextView tvMovieName;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private MoviePresenter presenter;

    // TODO sprav frontu
    private Movie actualMovie;
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

    @OnClick(R.id.bLike)
    public void likeClick() {
        presenter.onLikeMovie(actualMovie);
    }

    @OnClick(R.id.bDislike)
    public void dislikeClick() {
        presenter.onDislikeMovie(actualMovie);
    }

    @OnClick(R.id.bStartService)
    public void startServiceClick() {
        MovieService.startActionStorePreference(this, "Lollipop");
    }

    @OnClick(R.id.bAskBindedService)
    public void askBindedServiceClick() {
        EventBus.getDefault().post(new BindingMovieService.AskForNameRequest());
    }

    public void onEvent(BindingMovieService.AskForNameResponse event) {
        Log.d(TAG, "onEvent " + event.name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        bindService(new Intent(this, BindingMovieService.class), mConnection, Context.BIND_AUTO_CREATE);

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
        EventBus.getDefault().register(this);

        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
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
        actualMovie = movie;
        tvMovieName.setText(movie.name);
    }
}
