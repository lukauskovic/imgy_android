package com.imgy.luka.imgy.Activities;

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

import com.imgy.luka.imgy.Activities.feed_activity.Feed;
import com.imgy.luka.imgy.Networking.RegisterRequest;
import com.imgy.luka.imgy.R;
import com.imgy.luka.imgy.Utils.DisplayToast;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import static com.imgy.luka.imgy.Constants.AppConstants.DEFAULT_ERROR_MESSAGE;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private EditText emailInput;
    private EditText passwordInput;
    private EditText repeatPasswordInput;
    private Button registerButton;
    private TextView goToSignInLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialiseUI();
    }

    private void initialiseUI() {
        this.emailInput = (EditText) findViewById(R.id.emailInput);
        this.passwordInput = (EditText) findViewById(R.id.passwordInput);
        this.registerButton = (Button) findViewById(R.id.registerButton);
        this.goToSignInLabel = (TextView) findViewById(R.id.goToSignInLabel);
        this.repeatPasswordInput = (EditText) findViewById(R.id.repeatPasswordInput);
        this.registerButton.setOnClickListener(this);
        this.goToSignInLabel.setOnClickListener(this);
    }

    public boolean checkFields() {
        if (TextUtils.isEmpty(emailInput.getText().toString())) {
            emailInput.setError("Please enter email");
            return false;
        } else if (TextUtils.isEmpty(passwordInput.getText().toString())) {
            passwordInput.setError("Please Enter password");
            return false;
        } else if (TextUtils.isEmpty(repeatPasswordInput.getText().toString())) {
            repeatPasswordInput.setError("Please Repeat password");
            return false;
        } else if (!passwordInput.getText().toString().trim().equals(repeatPasswordInput.getText().toString().trim())) {
            repeatPasswordInput.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    public static void onSuccessfulRegistration(Response response, Activity register) {
        try {
            JSONObject responseBody = new JSONObject(response.body().string());
            String token = responseBody.getString("token");
            SharedPreferences pref = register.getSharedPreferences("prefs", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("auth_token", token); // Storing string
            editor.apply();
            Intent feedIntent = new Intent(register, Feed.class);
            register.startActivity(feedIntent);
        } catch (Exception e) {
            e.printStackTrace();
            new DisplayToast(register, DEFAULT_ERROR_MESSAGE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == registerButton) {
            if (!checkFields()) return;
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            registerButton.setEnabled(false);
            new RegisterRequest(Register.this).execute(email, password);
            registerButton.setEnabled(true);

        }
        if (view == goToSignInLabel) {
            Intent signUpIntent = new Intent(this.getApplicationContext(), Login.class);
            this.startActivity(signUpIntent);
        }
    }
}
