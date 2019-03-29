package com.imgy.luka.imgy.networking;

import android.app.Activity;
import android.os.AsyncTask;

import com.imgy.luka.imgy.activities.Register;
import com.imgy.luka.imgy.constants.AppConstants;
import com.imgy.luka.imgy.utils.DisplayToast;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import static com.imgy.luka.imgy.constants.AppConstants.DEFAULT_ERROR_MESSAGE;
import static com.imgy.luka.imgy.constants.AppConstants.EMAIL_ALREADY_REGISTERED_MESSAGE;

public class RegisterRequest extends AsyncTask<String, Integer, Response> {

    private WeakReference<Activity> registerActivity;
    private OkHttpClient client = new OkHttpClient();
    private Response response;

    public RegisterRequest(Activity activity) {
        this.registerActivity = new WeakReference<>(activity);
    }

    @Override
    protected Response doInBackground(String... strings) {
        try {
            String data = new JSONObject()
                    .put("email", strings[0])
                    .put("password", strings[1])
                    .toString();
            RequestBody body = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("data", data)
                    .build();

            Request request = new Request.Builder()
                    .header("Authorization-static", AppConstants.STATIC_TOKEN)
                    .header("Accept-Encoding", "gzip")
                    .post(body)
                    .url(AppConstants.URL + "/auth/sign-up")
                    .build();

            response = client.newCall(request).execute();

        } catch (Exception e) {
            e.printStackTrace();
            new DisplayToast(registerActivity.get(), DEFAULT_ERROR_MESSAGE);
        }
        return response;
    }

    protected void onPostExecute(Response response) {
        if (response != null && response.code() == 200) {
            Register.onSuccessfulRegistration(response, registerActivity.get());
        } else if (response != null && response.code() == 409) {
            new DisplayToast(registerActivity.get(), EMAIL_ALREADY_REGISTERED_MESSAGE);
        } else new DisplayToast(registerActivity.get(), DEFAULT_ERROR_MESSAGE);
    }
}
