package com.imgy.luka.imgy.Utils;

import android.app.Activity;
import android.widget.Toast;

public class DisplayToast {

    public DisplayToast(Activity activity, final String message){
        activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show());
    }
}
