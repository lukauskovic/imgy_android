package com.imgy.luka.imgy.networking;

import android.app.Activity;
import android.os.AsyncTask;

import com.imgy.luka.imgy.activities.Feed;
import com.imgy.luka.imgy.constants.AppConstants;
import com.imgy.luka.imgy.objects.Item;
import com.imgy.luka.imgy.utils.DisplayToast;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.imgy.luka.imgy.constants.AppConstants.DEFAULT_ERROR_MESSAGE;
import static com.imgy.luka.imgy.constants.AppConstants.FEED_TAKE_VALUE;

public class GetFeedItems extends AsyncTask<Integer,Integer,ArrayList<Item>> {

    private OkHttpClient client = new OkHttpClient();
    private ArrayList<Item> data = new ArrayList<>();
    private final WeakReference<Activity> feedActivity;
    private int page;

    public GetFeedItems(Activity feedActivity){
        this.feedActivity = new WeakReference<>(feedActivity);
    }

    @Override
    protected ArrayList<Item> doInBackground(Integer... pages) {
        try {
            page = pages[0];
            String description;
            String username;
            String imageUrl;
            Request request = new Request.Builder()
                    .header("Authorization-static", AppConstants.STATIC_TOKEN)
                    .header("Accept-Encoding", "gzip")
                    .url(AppConstants.URL + "/feed?take=" + FEED_TAKE_VALUE + "&page=" + page)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                JSONObject responseBody = new JSONObject(response.body().string());
                JSONArray feedItems = (JSONArray) responseBody.get("feedItems");

                for (int i = 0; i < feedItems.length(); i++) {
                    description = feedItems.getJSONObject(i).getString("description");
                    String user = feedItems.getJSONObject(i).getString("user");
                    JSONObject userObject = new JSONObject(user);
                    username = userObject.getString("username");
                    String userId = userObject.getString("_id");
                    imageUrl = feedItems.getJSONObject(i).getString("images");
                    imageUrl = imageUrl.substring(0, imageUrl.length() - 2);
                    imageUrl = imageUrl.substring(2);
                    data.add(i, new Item(username, imageUrl, description, userId));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            new DisplayToast(feedActivity.get(), DEFAULT_ERROR_MESSAGE);
        }

        return data;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(ArrayList<Item> result) {
        if(result.size() != 0) {
            if (page == 1) Feed.initAdapter(feedActivity.get(), result);
            else Feed.updateAdapter(result);
        }

    }
}
