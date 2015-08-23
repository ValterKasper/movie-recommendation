package sk.kasper.nottoday.movie;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Manipulates with UI
 */
public class MoviePresenter {
    IMovieInteractor movieInteractor;
    private IMovieView movieView;

    public MoviePresenter(IMovieView movieView, MovieInteractor movieInteractor) {
        this.movieView = movieView;
        this.movieInteractor = movieInteractor;
    }

    public void onResume() {
        movieView.showProgressBar();
        movieInteractor.loadMovie(12345)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Movie>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Movie movie) {
                        movieView.showMovieInfo(movie);
                        movieView.hideProgressBar();
                    }
                });
    }

    public void onLikeClick() {

    }

    public void onDislikeClick() {

    }
}
