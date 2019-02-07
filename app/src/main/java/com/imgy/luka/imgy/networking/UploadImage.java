package com.imgy.luka.imgy.networking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.imgy.luka.imgy.activities.Upload;
import com.imgy.luka.imgy.activities.feed_activity.Feed;
import com.imgy.luka.imgy.constants.AppConstants;
import com.imgy.luka.imgy.objects.FeedItem;
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

public class UploadImage extends AsyncTask<String , Integer, Response> {

    private WeakReference<Activity> uploadActivity;
    private OkHttpClient client = new OkHttpClient();
    private Response response;

    public UploadImage(Activity activity) {
        this.uploadActivity = new WeakReference<>(activity);
    }

    @Override
    protected Response doInBackground(String... strings) {
        try {
            final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
            SharedPreferences pref = uploadActivity.get().getSharedPreferences("prefs", 0);
            String authToken = pref.getString("auth_token", "");
            String data = new JSONObject()
                    .put("description", strings[1])
                    .toString();
            File image = new File(strings[0]);
            RequestBody body = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("data", data)
                    .addFormDataPart("images", image.getName() ,RequestBody.create(MEDIA_TYPE_JPG, image))
                    .build();

            Request request = new Request.Builder()
                    .header("Authorization-static", AppConstants.STATIC_TOKEN)
                    .header("Accept-Encoding", "gzip")
                    .header("Authorization", authToken)
                    .post(body)
                    .url(AppConstants.URL + "/feed")
                    .build();

            response = client.newCall(request).execute();

        } catch (Exception e) {
            e.printStackTrace();
            new DisplayToast(uploadActivity.get(), DEFAULT_ERROR_MESSAGE);
        }
        return response;
    }

    protected void onPostExecute(Response response){
    if (response.code() == 200){
        Intent feedIntent = new Intent(uploadActivity.get(), Feed.class);
        uploadActivity.get().startActivity(feedIntent);
    } else new DisplayToast(uploadActivity.get(), DEFAULT_ERROR_MESSAGE);
    }

}
