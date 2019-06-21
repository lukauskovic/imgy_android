package com.imgy.luka.imgy.networking;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.imgy.luka.imgy.activities.Feed;
import com.imgy.luka.imgy.activities.PublicProfile;
import com.imgy.luka.imgy.constants.AppConstants;
import com.imgy.luka.imgy.objects.User;
import com.imgy.luka.imgy.utils.DisplayToast;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.imgy.luka.imgy.constants.AppConstants.DEFAULT_ERROR_MESSAGE;
public class LikeRequest extends AsyncTask<String,Integer,Response> {

    private OkHttpClient client = new OkHttpClient();
    private Response response;
    private final WeakReference<Activity> activity;
    private User user;

    public LikeRequest(WeakReference activity){
        this.activity = activity;
    }

    @Override
    protected Response doInBackground(String... ids) {
        try {

            String likedItemId = ids[0];
            String data = new JSONObject()
                    .toString();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, data);

            SharedPreferences pref = activity.get().getSharedPreferences("prefs", 0);
            String authToken = pref.getString("auth_token", "");

            Request request = new Request.Builder()
                    .header("Authorization", authToken)
                    .put(body)
                    .url(AppConstants.URL + "/feed/item/like?feedItemId=" + likedItemId)
                    .build();

            response = client.newCall(request).execute();
            JSONObject responseBody = new JSONObject(response.body().string());

            Integer likesCount = responseBody.getInt("likesCount");
            JSONArray likesJson= responseBody.getJSONArray("likes");
            ArrayList<String> likes = new ArrayList<String>();
            for (int i = 0; i< likesJson.length(); i++){
                likes.add(likesJson.get(i).toString());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            new DisplayToast(activity.get(), DEFAULT_ERROR_MESSAGE);
        }

        return response;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Response response) {

    }
}
