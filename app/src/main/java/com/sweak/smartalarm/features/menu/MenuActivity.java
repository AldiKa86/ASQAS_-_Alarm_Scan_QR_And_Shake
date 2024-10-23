package com.sweak.smartalarm.features.menu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sweak.smartalarm.R;
import com.sweak.smartalarm.SmartAlarmApplication;
import com.sweak.smartalarm.databinding.ActivityMenuBinding;
import com.sweak.smartalarm.features.menu.about.AboutActivity;
import com.sweak.smartalarm.features.scan.ScanActivity;
import com.sweak.smartalarm.util.AlarmPlayer;
import com.sweak.smartalarm.util.Preferences;

public class MenuActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Preferences mPreferences;
    private AlarmPlayer mAlarmPlayer;
    private ActivityMenuBinding mBinding;
    ArrayAdapter<CharSequence> mAlarmToneAdapter;
    ArrayAdapter<CharSequence> mSnoozeDurationAdapter;
    ArrayAdapter<CharSequence> mSnoozeNumberAdapter;
    ArrayAdapter<CharSequence> mShakeCountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mPreferences = new Preferences(getApplication());
        mAlarmPlayer = new AlarmPlayer(getApplication());
        prepareViews();
        setSpinnerListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mAlarmPlayer.stop();
    }

    private void prepareViews() {
        mAlarmToneAdapter = ArrayAdapter.createFromResource(this,
                R.array.alarm_tones, android.R.layout.simple_spinner_item);
        mSnoozeDurationAdapter = ArrayAdapter.createFromResource(this,
                R.array.snooze_durations, android.R.layout.simple_spinner_item);
        mSnoozeNumberAdapter = ArrayAdapter.createFromResource(this,
                R.array.snooze_numbers, android.R.layout.simple_spinner_item);
        mShakeCountAdapter = ArrayAdapter.createFromResource(this,
                R.array.shake_counts, android.R.layout.simple_spinner_item);

        mAlarmToneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSnoozeDurationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSnoozeNumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mShakeCountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mBinding.alarmToneSpinner.setAdapter(mAlarmToneAdapter);
        mBinding.snoozeDurationSpinner.setAdapter(mSnoozeDurationAdapter);
        mBinding.snoozeNumberSpinner.setAdapter(mSnoozeNumberAdapter);
        mBinding.shakeCountSpinner.setAdapter(mShakeCountAdapter);

        mBinding.alarmToneSpinner.setSelection(mPreferences.getAlarmToneId());
        mBinding.snoozeDurationSpinner.setSelection(mSnoozeDurationAdapter.getPosition(
                String.valueOf(mPreferences.getSnoozeDuration())));
        mBinding.snoozeNumberSpinner.setSelection(mSnoozeNumberAdapter.getPosition(
                String.valueOf(mPreferences.getSnoozeNumber())));
        mBinding.shakeCountSpinner.setSelection(mShakeCountAdapter.getPosition(
                String.valueOf(mPreferences.getShakeCount())));
    }

    private void setSpinnerListeners() {
        mBinding.alarmToneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPreferences.setAlarmToneId(position);
                mAlarmPlayer.stop();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mBinding.snoozeDurationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPreferences.setSnoozeDuration(Integer.parseInt(
                        parent.getItemAtPosition(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mBinding.snoozeNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int snoozeNumber = Integer.parseInt(parent.getItemAtPosition(position).toString());
                mPreferences.setSnoozeNumber(snoozeNumber);
                mPreferences.setSnoozeNumberLeft(snoozeNumber);
                mBinding.snoozeDurationSpinner.setEnabled(snoozeNumber != 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mBinding.shakeCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPreferences.setShakeCount(Integer.parseInt(parent.getItemAtPosition(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void showAboutDialog(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
//        DialogFragment aboutDialog = AboutDialogFragment.newInstance();
//        aboutDialog.show(getSupportFragmentManager(), "ABOUT_DIALOG");
    }

    public void startAlarmPreview(View view) {
        mAlarmPlayer.startPreview(mPreferences.getAlarmToneId());
    }
}