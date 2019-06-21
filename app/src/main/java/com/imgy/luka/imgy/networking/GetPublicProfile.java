package com.imgy.luka.imgy.networking;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.imgy.luka.imgy.activities.PublicProfile;
import com.imgy.luka.imgy.constants.AppConstants;
import com.imgy.luka.imgy.objects.Item;
import com.imgy.luka.imgy.objects.User;
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
import static com.imgy.luka.imgy.constants.AppConstants.STATIC_TOKEN;

public class GetPublicProfile extends AsyncTask<Integer, Integer, ArrayList<Item>> {

    private OkHttpClient client = new OkHttpClient();
    private ArrayList<Item> data = new ArrayList<>();
    private final WeakReference<Activity> publicProfileActivity;
    private User user;
    private String userId;
    private int page;

    public GetPublicProfile(Activity publicProfileActivity, String userId){
        this.publicProfileActivity = new WeakReference<>(publicProfileActivity);
        this.userId = userId;
    }

    @Override
    protected ArrayList<Item> doInBackground(Integer... pages) {
        try {
            page = pages[0];
            String description;
            String username;
            String imageUrl;
            SharedPreferences pref = publicProfileActivity.get().getSharedPreferences("prefs", 0);
            String authToken = pref.getString("auth_token", "");
            Request request = new Request.Builder()
                    .header("Authorization", authToken)
                    .header("Accept-Encoding", "gzip")
                    .url(AppConstants.URL + "/user?userId=" + userId + "&take=" + FEED_TAKE_VALUE + "&page=" + page)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                JSONObject responseBody = new JSONObject(response.body().string());
                if (page == 1){
                    JSONObject foundUser = (JSONObject) responseBody.get("foundUser");
                    String foundUsername = foundUser.getString("username");
                    String followersCount = foundUser.getString("followersCount");
                    String followingCount = foundUser.getString("followingCount");
                    String photosCount = responseBody.getString("foundImagesCount");
                    String id = foundUser.getString("_id");

                    JSONArray followersJson= foundUser.getJSONArray("followers");
                    ArrayList<String> followers = new ArrayList<String>();
                    for (int i = 0; i< followersJson.length(); i++){
                        followers.add(followersJson.get(i).toString());
                    }

                    user = new User( id, foundUsername, followersCount, followingCount, photosCount, followers);
                    if(foundUser.has("profileImage")){
                        user.setProfileImageUrl(foundUser.getString("profileImage"));
                    }
                }

                JSONArray foundImages = (JSONArray) responseBody.get("foundImages");
                for (int i = 0; i < foundImages.length(); i++) {
                    description = foundImages.getJSONObject(i).getString("description");
                    String user = foundImages.getJSONObject(i).getString("user");
                    Integer likesCount = foundImages.getJSONObject(i).getInt("likesCount");
                    JSONObject userObject = new JSONObject(user);
                    username = userObject.getString("username");
                    String itemId = foundImages.getJSONObject(i).getString("_id");
                    imageUrl = foundImages.getJSONObject(i).getString("images");
                    imageUrl = imageUrl.substring(0, imageUrl.length() - 2);
                    imageUrl = imageUrl.substring(2);

                    JSONArray likesJson = foundImages.getJSONObject(i).getJSONArray("likes");
                    ArrayList<String> likes = new ArrayList<>();
                    for (int p = 0; p< likesJson.length(); p++){
                        likes.add(likesJson.get(p).toString());
                    }
                    data.add(i, new Item(itemId, username, imageUrl, description, likesCount, likes));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            new DisplayToast(publicProfileActivity.get(), DEFAULT_ERROR_MESSAGE);
        }
        return data;
    }

    protected void onPostExecute(ArrayList<Item> result) {
        if (page ==1){
            PublicProfile.setUser(user);
            PublicProfile.initAdapter(publicProfileActivity, result);
        }
        else PublicProfile.updateAdapter(result);
    }
}
