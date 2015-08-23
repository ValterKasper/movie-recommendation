package sk.kasper.nottoday;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;
import sk.kasper.nottoday.api.NotTodayApi;
import sk.kasper.nottoday.api.NotTodayService;

/**
 * Created by Valter on 01.08.2015.
 */
public class NotTodayApplication extends Application {

	private NotTodayService notTodayService;

	public NotTodayApi getNotTodayApi() {
		return notTodayApi;
	}

	private NotTodayApi notTodayApi;

	@Override
	public void onCreate() {
		super.onCreate();
		notTodayApi = Utils.getApi();
		notTodayService = new NotTodayService(notTodayApi);
		EventBus.getDefault().register(notTodayService);
	}
}
