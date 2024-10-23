package com.sweak.smartalarm.features.shake;

import static com.sweak.smartalarm.util.AlarmSetter.REGULAR_ALARM;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sweak.smartalarm.R;
import com.sweak.smartalarm.databinding.ActivityShakeWqrBinding;
import com.sweak.smartalarm.features.main.FinishWqrActivity;
import com.sweak.smartalarm.util.AlarmSetter;

public class ShakeWqrActivity extends AppCompatActivity implements SensorEventListener {

    private Vibrator vibrator;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ActivityShakeWqrBinding mBinding;
    private int shakeCount = 0;
    private long lastShakeTime = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_wqr);

        mBinding = ActivityShakeWqrBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            double acceleration = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;
            TextView tvShakeCount = findViewById(R.id.tv_shake_wqr_count);
            if (acceleration > 2) {
                long now = System.currentTimeMillis();

                if (now - lastShakeTime > 500) {
                    shakeCount++;
                    lastShakeTime = now;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                    }
                }
            }

            tvShakeCount.setText("Shake the device 20 times to turn off the alarm\n\n" + shakeCount);
            if (shakeCount >= 20) {
                AlarmSetter.cancelAlarm(getApplication(), REGULAR_ALARM);
                Intent intent = new Intent(this, FinishWqrActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not yet implemented
    }
}