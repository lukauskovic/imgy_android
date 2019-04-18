package com.imgy.luka.imgy.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.imgy.luka.imgy.R;
import com.imgy.luka.imgy.networking.ChangePasswordRequest;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {

    private EditText oldPasswordInput;
    private EditText newPasswordInput;
    private EditText repeatNewPasswordInput;
    private Button changePasswordButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initialiseUI();
    }

    public void initialiseUI() {
        oldPasswordInput = findViewById(R.id.oldPassword);
        newPasswordInput = findViewById(R.id.newPasswordInput);
        repeatNewPasswordInput = findViewById(R.id.repeatNewPasswordInput);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(this);
    }

    private boolean checkFields() {
        if (TextUtils.isEmpty(oldPasswordInput.getText().toString())) {
            oldPasswordInput.setError("Please enter password");
            return false;
        } else if (TextUtils.isEmpty(newPasswordInput.getText().toString())) {
            newPasswordInput.setError("Please enter password");
            return false;
        } else if (TextUtils.isEmpty(repeatNewPasswordInput.getText().toString())) {
            repeatNewPasswordInput.setError("Please enter password");
            return false;
        } else if (!newPasswordInput.getText().toString().trim().equals(repeatNewPasswordInput.getText().toString().trim())) {
            repeatNewPasswordInput.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == changePasswordButton) {
            if (!checkFields()) return;
        changePasswordButton.setEnabled(false);
        String oldPassword = oldPasswordInput.getText().toString().trim();
        String newPassword = newPasswordInput.getText().toString().trim();
        new ChangePasswordRequest(this).execute(oldPassword, newPassword);
        changePasswordButton.setEnabled(true);
        }
    }
}
