package sk.kasper.movieapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import de.greenrobot.event.EventBus;

public class BindingMovieService extends Service {
    int index = 0;
    String[] names = {"KitKat", "Honey Comb", "Marshmallow"};

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public String saySomething() {
        return names[index++ % names.length];
    }

    public void onEvent(AskForNameRequest event) {
        EventBus.getDefault().post(new AskForNameResponse(saySomething()));
    }

    public static class AskForNameRequest {
    }

    public static class AskForNameResponse {
        public String name;

        public AskForNameResponse(String s) {
            this.name = s;
        }
    }
}
