package com.imgy.luka.imgy.networking;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
public class FollowRequest extends AsyncTask<String,Integer,Response> {

    private OkHttpClient client = new OkHttpClient();
    private Response response;
    private final WeakReference<Activity> publicProfileActivity;
    private User user;

    public FollowRequest(Activity activity){
        this.publicProfileActivity = new WeakReference<>(activity);
    }

    @Override
    protected Response doInBackground(String... ids) {
        try {

            String followId = ids[0];
            String data = new JSONObject()
                    .toString();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, data);

            SharedPreferences pref = publicProfileActivity.get().getSharedPreferences("prefs", 0);
            String authToken = pref.getString("auth_token", "");

            Request request = new Request.Builder()
                    .header("Authorization", authToken)
                    .put(body)
                    .url(AppConstants.URL + "/user/follow?userId=" + followId)
                    .build();

            response = client.newCall(request).execute();
            JSONObject responseBody = new JSONObject(response.body().string());
            JSONObject foundUser = (JSONObject) responseBody.get("followedUser");
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
        catch (Exception e) {
            e.printStackTrace();
            new DisplayToast(publicProfileActivity.get(), DEFAULT_ERROR_MESSAGE);
        }

        return response;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Response response) {
        if(response.code() == 200) {
            PublicProfile.setUser(user);
            PublicProfile.onSuccessfulFollow();
        }

    }
}
