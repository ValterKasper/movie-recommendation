package sk.kasper.movieapp.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class MovieService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_STORE_PREFERENCES = "sk.kasper.movieapp.services.action.STORE_PREFERENCES";
    private static final String EXTRA_NAME_OF_MOVIE = "sk.kasper.movieapp.services.extra.NAME_OF_MOVIE";
    private static final String TAG = "MovieService";

    public MovieService() {
        super("MovieService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionStorePreference(Context context, String param1) {
        Intent intent = new Intent(context, MovieService.class);
        intent.setAction(ACTION_STORE_PREFERENCES);
        intent.putExtra(EXTRA_NAME_OF_MOVIE, param1);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_STORE_PREFERENCES.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_NAME_OF_MOVIE);
                handleActionStorePresences(param1);
            }
        }
    }

    /**
     * Handle action Store preferences in the provided background thread with the provided
     * parameters.
     */
    private void handleActionStorePresences(String param1) {
        Log.d(TAG, "handleActionStorePresences " + param1);
    }
}
