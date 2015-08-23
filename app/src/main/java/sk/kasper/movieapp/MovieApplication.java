package sk.kasper.movieapp;

import android.app.Application;

import de.greenrobot.event.EventBus;
import sk.kasper.movieapp.api.NotTodayApi;
import sk.kasper.movieapp.api.NotTodayService;

/**
 * Created by Valter on 01.08.2015.
 */
public class MovieApplication extends Application {

	private NotTodayService notTodayService;
	private NotTodayApi notTodayApi;

	public NotTodayApi getNotTodayApi() {
		return notTodayApi;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		notTodayApi = Utils.getApi();
		notTodayService = new NotTodayService(notTodayApi);
		EventBus.getDefault().register(notTodayService);
	}
}
