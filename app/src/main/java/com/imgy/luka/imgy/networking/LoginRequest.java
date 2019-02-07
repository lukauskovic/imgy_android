package com.imgy.luka.imgy.networking;

import android.app.Activity;
import android.os.AsyncTask;

import com.imgy.luka.imgy.activities.Login;
import com.imgy.luka.imgy.constants.AppConstants;
import com.imgy.luka.imgy.utils.DisplayToast;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import static com.imgy.luka.imgy.constants.AppConstants.DEFAULT_ERROR_MESSAGE;

public class LoginRequest extends AsyncTask<String, Integer, Response> {

    private OkHttpClient client = new OkHttpClient();
    private final WeakReference<Activity> loginActivity;
    private Response response;

    public LoginRequest(Activity activity){
        this.loginActivity = new WeakReference<>(activity);
    }

    @Override
    protected Response doInBackground(String... strings) {
        try{
            String data = new JSONObject()
                    .put("email", strings[0])
                    .put("password", strings[1])
                    .toString();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, data);

            Request request = new Request.Builder()
                    .header("Authorization-static", AppConstants.STATIC_TOKEN)
                    .header("Accept-Encoding", "gzip")
                    .post(body)
                    .url(AppConstants.URL + "/auth/sign-in")
                    .build();
            response = client.newCall(request).execute();
        }catch(Exception e){
            e.printStackTrace();
            new DisplayToast(loginActivity.get(), DEFAULT_ERROR_MESSAGE);
        }
        return response;
    }

    protected void onPostExecute(Response response){
        if (response.code() == 200){
            Login.onSuccessfulLogin(response, loginActivity.get());
        }
        else new DisplayToast(loginActivity.get(), DEFAULT_ERROR_MESSAGE);
    }
}
