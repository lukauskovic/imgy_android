package com.imgy.luka.imgy.networking;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import com.imgy.luka.imgy.activities.MyProfile;
import com.imgy.luka.imgy.constants.AppConstants;
import com.imgy.luka.imgy.utils.DisplayToast;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;

import static com.imgy.luka.imgy.constants.AppConstants.DEFAULT_ERROR_MESSAGE;

public class UploadProfilePicture extends AsyncTask<Uri, Integer, Response> {

    private WeakReference<Activity> profileActivity;
    private OkHttpClient client = new OkHttpClient();
    private Response response;
    private String profileImageUrl;

    public UploadProfilePicture(Activity activity) {
        this.profileActivity = new WeakReference<>(activity);
    }

    @Override
    protected Response doInBackground(Uri ... uris) {
        try {
            final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
            SharedPreferences pref = profileActivity.get().getSharedPreferences("prefs", 0);
            String authToken = pref.getString("auth_token", "");
            Uri imageUri = uris[0];
            File image = new File(imageUri.getPath());
            String data = new JSONObject()
                    .put("username", "john@doe.com") // fix this on backend
                    .toString();
            RequestBody body = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("data", data)
                    .addFormDataPart("image", image.getName() ,RequestBody.create(MEDIA_TYPE_JPG, image))
                    .build();

            Request request = new Request.Builder()
                    .header("Authorization-static", AppConstants.STATIC_TOKEN)
                    .header("Accept-Encoding", "gzip")
                    .header("Authorization", authToken)
                    .put(body)
                    .url(AppConstants.URL + "/user")
                    .build();

            response = client.newCall(request).execute();

            JSONObject responseBody = new JSONObject(response.body().string());
            profileImageUrl = responseBody.getString("profileImage");

        } catch (Exception e) {
            e.printStackTrace();
            new DisplayToast(profileActivity.get(), DEFAULT_ERROR_MESSAGE);
        }
        return response;
    }

    protected void onPostExecute(Response response){
        if (response.code() == 200){
            MyProfile.onProfileImageChanged(profileImageUrl);
        } else new DisplayToast(profileActivity.get(), DEFAULT_ERROR_MESSAGE);
    }
}
