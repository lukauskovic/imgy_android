package com.imgy.luka.imgy.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imgy.luka.imgy.activities.feed_activity.Feed;
import com.imgy.luka.imgy.networking.LoginRequest;
import com.imgy.luka.imgy.R;
import com.imgy.luka.imgy.utils.DisplayToast;
import com.squareup.okhttp.Response;
import org.json.JSONObject;

import static com.imgy.luka.imgy.constants.AppConstants.DEFAULT_ERROR_MESSAGE;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText emailInput;
    private EditText passwordInput;
    private Button signInButton;
    private TextView goToRegisterLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialiseUI();
    }

    public static void onSuccessfulLogin(Response response, Activity login){
        try{
            JSONObject responseBody = new JSONObject(response.body().string());
            String token = responseBody.getString("token");
            SharedPreferences pref = login.getSharedPreferences("prefs", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("auth_token", token); // Storing string
            editor.apply();
            Intent feedIntent = new Intent(login.getApplicationContext(), Feed.class);
            login.startActivity(feedIntent);
        }catch (Exception e){
            e.printStackTrace();
            new DisplayToast(login, DEFAULT_ERROR_MESSAGE);
        }

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

    @Override
    public void onClick(View view) {
        if(view == signInButton){
            if(!checkFields()) return;
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            signInButton.setEnabled(false);
            new LoginRequest(Login.this).execute(email, password);
            signInButton.setEnabled(true);
        }
         if (view == goToRegisterLabel){
             Intent registerIntent = new Intent(this.getApplicationContext(), Register.class);
             this.startActivity(registerIntent);
            }
        }
}
