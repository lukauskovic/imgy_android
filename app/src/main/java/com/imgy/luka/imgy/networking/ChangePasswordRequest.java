package com.imgy.luka.imgy.networking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.imgy.luka.imgy.activities.Register;
import com.imgy.luka.imgy.constants.AppConstants;
import com.imgy.luka.imgy.utils.DisplayToast;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import static com.imgy.luka.imgy.constants.AppConstants.CHANGE_PASSWORD_SUCCESS;
import static com.imgy.luka.imgy.constants.AppConstants.DEFAULT_ERROR_MESSAGE;
import static com.imgy.luka.imgy.constants.AppConstants.EMAIL_ALREADY_REGISTERED_MESSAGE;

public class ChangePasswordRequest extends AsyncTask<String, Integer, Response> {

    private WeakReference<Activity> changePasswordActivity;
    private OkHttpClient client = new OkHttpClient();
    private Response response;

    public ChangePasswordRequest(Activity activity) {
        this.changePasswordActivity = new WeakReference<>(activity);
    }

    @Override
    protected Response doInBackground(String... strings) {
        try {
            String data = new JSONObject()
                    .put("oldPassword", strings[0])
                    .put("newPassword", strings[1])
                    .toString();

            SharedPreferences pref = changePasswordActivity.get().getSharedPreferences("prefs", 0);
            String authToken = pref.getString("auth_token", "");

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, data);

            Request request = new Request.Builder()
                    .header("Authorization", authToken)
                    .post(body)
                    .url(AppConstants.URL + "/auth/change-password")
                    .build();

            response = client.newCall(request).execute();

        } catch (Exception e) {
            e.printStackTrace();
            new DisplayToast(changePasswordActivity.get(), DEFAULT_ERROR_MESSAGE);
        }
        return response;
    }

    protected void onPostExecute(Response response) {
        if (response != null && response.code() == 200) {
            changePasswordActivity.get().finish();
            new DisplayToast(changePasswordActivity.get(), CHANGE_PASSWORD_SUCCESS);
        } else new DisplayToast(changePasswordActivity.get(), DEFAULT_ERROR_MESSAGE);
    }
}
