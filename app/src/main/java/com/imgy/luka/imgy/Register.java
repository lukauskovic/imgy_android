package com.imgy.luka.imgy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imgy.luka.imgy.Constants.AppConstants;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private EditText emailInput;
    private EditText passwordInput;
    private EditText repeatPasswordInput;
    private Button registerButton;
    private TextView goToSignInLabel;
    OkHttpClient client = new OkHttpClient();
    Intent registrationIntent = new Intent(this, Register.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       initialiseUI();
    }

    private void initialiseUI(){
        this.emailInput = (EditText) findViewById(R.id.emailInput);
        this.passwordInput = (EditText) findViewById(R.id.passwordInput);
        this.registerButton = (Button) findViewById(R.id.registerButton);
        this.goToSignInLabel = (TextView) findViewById(R.id.goToSignInLabel);
        this.repeatPasswordInput = (EditText) findViewById(R.id.repeatPasswordInput);
        this.registerButton.setOnClickListener(this);
        this.goToSignInLabel.setOnClickListener(this);
    }

    public boolean checkFields(){
        if(TextUtils.isEmpty(emailInput.getText().toString())) {
            emailInput.setError("Please enter email");
            return false;
        }
        else if(TextUtils.isEmpty(passwordInput.getText().toString())) {
            passwordInput.setError("Please Enter password");
            return false;
        }
        else if(TextUtils.isEmpty(repeatPasswordInput.getText().toString())){
            repeatPasswordInput.setError("Please Repeat password");
            return false;
        }
        else if(!passwordInput.getText().toString().trim().equals(repeatPasswordInput.getText().toString().trim())){
            repeatPasswordInput.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    public void Registration(final String email, final String password){
        new Thread(new Runnable() {
            public void run() {
                try{
                    String data = new JSONObject()
                            .put("email", email)
                            .put("password", password)
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
                     else if (response.code() == 409){
                        displayToast("Email already registered");
                        String token = response.body().string();
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
        if (view == registerButton){
            if (!checkFields()) return;
            registerButton.setEnabled(false);
            Registration(emailInput.getText().toString().trim(), passwordInput.getText().toString().trim());
            registerButton.setEnabled(true);

        }
        if (view == goToSignInLabel){
            Intent signUpIntent = new Intent(this.getApplicationContext(), Login.class);
            this.startActivity(signUpIntent);
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
