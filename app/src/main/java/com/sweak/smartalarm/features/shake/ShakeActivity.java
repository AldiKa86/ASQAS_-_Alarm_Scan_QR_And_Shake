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
import com.sweak.smartalarm.databinding.ActivityMenuBinding;
import com.sweak.smartalarm.databinding.ActivityShakeBinding;
import com.sweak.smartalarm.features.main.FinishActivity;
import com.sweak.smartalarm.util.AlarmSetter;
import com.sweak.smartalarm.util.Preferences;

public class ShakeActivity extends AppCompatActivity implements SensorEventListener {

    private Preferences mPreferences;
    private Vibrator vibrator;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ActivityShakeBinding mBinding;
    private int shakeCount = 0;
    private long lastShakeTime = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        mBinding = ActivityShakeBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mPreferences = new Preferences(getApplication());
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
            TextView tvShakeCount = findViewById(R.id.tv_shake_count);
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

            tvShakeCount.setText("Shake the device " + mPreferences.getShakeCount() + " times to turn off the alarm\n\n" + shakeCount);
            if (shakeCount >= mPreferences.getShakeCount()) {
                AlarmSetter.cancelAlarm(getApplication(), REGULAR_ALARM);
                Intent intent = new Intent(this, FinishActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

//    private void showNotification() {
//        String id = "Smart Alarm";
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = mNotificationManager.getNotificationChannel(id);
//            if (channel == null) {
//                channel = new NotificationChannel(id, "After Alarm", NotificationManager.IMPORTANCE_HIGH);
//                channel.setDescription("You woke up on time");
//                channel.enableVibration(true);
//                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//                mNotificationManager.createNotificationChannel(channel);
//            }
//        }
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id)
//                .setSmallIcon(R.mipmap.alarm_icon)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.alarm_icon))
//                .setStyle(new NotificationCompat.BigTextStyle())
//                .setContentTitle(getResources().getString(R.string.notification_title))
//                .setContentText(getResources().getString(R.string.notification_message))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(false)
//                .setTicker("Notification");
//        builder.setContentIntent(contentIntent);
//        NotificationManagerCompat m = NotificationManagerCompat.from(getApplicationContext());
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        m.notify(1, builder.build());
//    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not yet implemented
    }
}
