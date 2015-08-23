package sk.kasper.movieapp.api;

import java.util.List;

import sk.kasper.movieapp.models.Item;

/**
 * Created by Valter on 08.08.2015.
 */
public class Responses {
    public static class LoadItemsResponse {
        public List<Item> items;
    }

    public static class DeleteItemResponse {
        public long id;

        public DeleteItemResponse(long i) {
            id = i;
        }
    }
}
