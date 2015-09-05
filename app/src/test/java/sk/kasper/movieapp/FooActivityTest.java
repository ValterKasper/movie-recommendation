package sk.kasper.movieapp;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import sk.kasper.movieapp.api.Requests;
import sk.kasper.movieapp.api.Responses;
import sk.kasper.movieapp.models.Item;

import static org.assertj.android.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class FooActivityTest {

    private FooActivity activity;
    private Button button;
    private ListView lvItems;
    private boolean requestCalled;
    private View vEmpty;

    @Before
    public void setup() throws Exception {
        activity = Robolectric.setupActivity(FooActivity.class);
        assertThat(activity).isNotNull();

        button = (Button) activity.findViewById(R.id.bAddItem);
        assertThat(button).isNotNull();

        lvItems = (ListView) activity.findViewById(R.id.lvItems);
        assertThat(lvItems).isNotNull();

        vEmpty = activity.findViewById(R.id.vEmpty);
        assertThat(vEmpty).isNotNull();

        requestCalled = false;
    }

    @Test
    public void listViewHasSetAdapter() throws Exception {
        assertThat(lvItems.getAdapter()).isNotNull();
    }

    @Test
    public void listViewIsInitiallyEmpty() throws Exception {
        assertEquals(lvItems.getAdapter().getCount(), 0);
    }

    @Test
    public void infoAboutEmptiesIsVisibleAndListIsGone() {
        assertThat(lvItems).isGone();
        assertThat(vEmpty).isVisible();
    }

    @Test
    public void recievedDataAreAddedToListView() {
        sendLoadDataResponse();
        assertEquals(lvItems.getAdapter().getCount(), 1);
    }

    private void sendLoadDataResponse() {
        final Responses.LoadItemsResponse response = new Responses.LoadItemsResponse();
        response.items = new ArrayList<>();
        final Item item = new Item();
        item.id = 42L;
        item.name = "Item1";
        response.items.add(item);
        activity.onEvent(response);
    }

    @Test
    public void visibilityOfViewAfterLoadingAnItem() {
        sendLoadDataResponse();
        assertThat(lvItems).isVisible();
        assertThat(vEmpty).isGone();
    }

    @Test
    public void listItemIsDeletedAfterResponse() {
        sendLoadDataResponse();
        assertEquals(lvItems.getAdapter().getCount(), 1);
        activity.onEvent(new Responses.DeleteItemResponse(42));
        assertEquals(lvItems.getAdapter().getCount(), 0);
    }

    @Test
    public void loadItemsRequestsIsCalled() throws Exception {
        EventBus.getDefault().register(this);

        ActivityController controller = Robolectric.buildActivity(FooActivity.class).create().start();
        FooActivity activity = (FooActivity) controller.get();
        assertEquals("Request was called", false, requestCalled);
        activity.onResume();
        assertEquals("Request wasn't called", true, requestCalled);
        EventBus.getDefault().unregister(this);
    }

    @Test
    public void afterClickOnAddButtonIsDialogShown() {
        button.performClick();
        Dialog dialog = ShadowAlertDialog.getLatestDialog();

        assertThat(dialog).isNotNull();
    }

    public void onEvent(Requests.LoadItemsRequest request) {
        requestCalled = true;
    }
}
