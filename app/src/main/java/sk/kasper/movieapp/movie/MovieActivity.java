package sk.kasper.movieapp.movie;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sk.kasper.movieapp.BaseActivity;
import sk.kasper.movieapp.R;

public class MovieActivity extends BaseActivity implements IMovieView{
    @Bind(R.id.tvMovieName)
    TextView tvMovieName;
    private MoviePresenter presenter;

    // TODO sprav frontu
    private Movie actualMovie;

    @OnClick(R.id.bLike)
    public void likeClick() {
        presenter.onLikeMovie(actualMovie);
    }

    @OnClick(R.id.bDislike)
    public void dislikeClick() {
        presenter.onDislikeMovie(actualMovie);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        presenter = new MoviePresenter(this, new MovieSuggestionEngineInteractorMock());
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
        actualMovie = movie;
        tvMovieName.setText(movie.name);
    }
}
