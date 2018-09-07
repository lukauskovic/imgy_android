package com.imgy.luka.imgy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.imgy.luka.imgy.Constants.AppConstants;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONObject;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText emailInput;
    private EditText passwordInput;
    private Button signInButton;
    private TextView goToRegisterLabel;
    OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialiseUI();
    }

    private void initialiseUI(){
        this.emailInput = (EditText) findViewById(R.id.emailInput);
        this.passwordInput = (EditText) findViewById(R.id.passwordInput);
        this.signInButton = (Button) findViewById(R.id.signInButton);
        this.goToRegisterLabel = (TextView) findViewById(R.id.goToRegisterLabel);
        this.signInButton.setOnClickListener(this);
        this.goToRegisterLabel.setOnClickListener(this);
    }

    public boolean checkFields(){
        if(TextUtils.isEmpty(emailInput.getText().toString())) {
            emailInput.setError("Please enter email");
            return false;
        }
        if(TextUtils.isEmpty(passwordInput.getText().toString())) {
            passwordInput.setError("Please Enter password");
            return false;
        }
        return true;
    }

    public void loginRequest(final String email, final String password){
        new Thread(new Runnable() {
            public void run() {
                try{
                    String data = new JSONObject()
                            .put("email", email)
                            .put("password", password)
                            .toString();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);

                    Request request = new Request.Builder()
                            .header("Authorization-static", AppConstants.STATIC_TOKEN)
                            .header("Accept-Encoding", "gzip")
                            .post(body)
                            .url(AppConstants.URL + "/auth/sign-in")
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.code() == 200){
                        JSONObject responseBody = new JSONObject(response.body().string());
                        String token = responseBody.getString("token");
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("prefs", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("auth_token", token); // Storing string
                        editor.apply();
                        Intent feedIntent = new Intent(getApplicationContext(), Feed.class);
                        startActivity(feedIntent);
                    }
                    else displayToast("Something went wrong");
                }catch(Exception e){
                    e.printStackTrace();
                    displayToast("Something went wrong");
                }
            }
        }).start();

    }


    @Override
    public void onClick(View view) {
        if(view == signInButton){
            if(!checkFields()) return;
            signInButton.setEnabled(false);
            loginRequest( emailInput.getText().toString().trim(), passwordInput.getText().toString().trim());
            signInButton.setEnabled(true);
        }
         if (view == goToRegisterLabel){
             Intent registerIntent = new Intent(this.getApplicationContext(), Register.class);
             this.startActivity(registerIntent);
            }
        }

    public void displayToast(final String message){
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
