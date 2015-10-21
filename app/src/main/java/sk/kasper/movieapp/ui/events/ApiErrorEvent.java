package sk.kasper.movieapp.ui.events;

/**
 * Created by Valter on 21.10.2015.
 */
public class ApiErrorEvent {
    public final String msg;

    public ApiErrorEvent(String msg) {
        this.msg = msg;
    }
}
