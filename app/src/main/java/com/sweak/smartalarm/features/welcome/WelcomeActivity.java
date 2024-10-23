package com.sweak.smartalarm.features.welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import com.sweak.smartalarm.R;
import com.sweak.smartalarm.features.menu.about.AboutActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = prefs.getBoolean("first_run", true);

        if (isFirstRun) {
            // Show the splash screen (e.g., display an image or animation)
            // You can use a Handler to delay launching the next activity
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(WelcomeActivity.this, AboutActivity.class);
                startActivity(intent);
                finish();
            }, 3000); // Delay in milliseconds (adjust as needed)

            // Mark that the splash screen has been shown
            prefs.edit().putBoolean("first_run", false).apply();
        } else {
            // The splash screen has already been shown, so directly launch the next activity
            Intent intent = new Intent(WelcomeActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
    }
}