package com.sweak.smartalarm.features.scan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sweak.smartalarm.R;
import com.sweak.smartalarm.SmartAlarmApplication;
import com.sweak.smartalarm.databinding.ActivityCustomBinding;
import com.sweak.smartalarm.databinding.ActivityMenuBinding;
import com.sweak.smartalarm.util.AlarmPlayer;
import com.sweak.smartalarm.util.Preferences;

public class CustomActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final int WRITE_GALLERY_PERMISSION_REQUEST_CODE = 1;

    private Preferences mPreferences;
    private ActivityCustomBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityCustomBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mPreferences = new Preferences(getApplication());
    }

    public void saveCodeToGallery(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (hasPermission(this)) {
                insertCodeImage();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_GALLERY_PERMISSION_REQUEST_CODE);
            }
        } else {
            Toast.makeText(this,
                            getString(R.string.code_not_added_to_gallery_version_issue),
                            Toast.LENGTH_LONG)
                    .show();
        }
    }

    private boolean hasPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void insertCodeImage() {
        String toastMessage;

        String savedUri = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                BitmapFactory.decodeResource(getResources(), R.drawable.qr_code),
                "SmartAlarm QR Code",
                "Code used to turn off the SmartAlarm alarm");

        toastMessage = savedUri != null ?
                getString(R.string.code_added_to_gallery)
                : getString(R.string.code_not_added_to_gallery);

        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }

    public void addCustomQRDismissCode(View view) {
        Intent addCodeIntent = new Intent(this, ScanActivity.class);
        addCodeIntent.putExtra(ScanActivity.SCAN_MODE_KEY, ScanActivity.MODE_SET_DISMISS_CODE);
        startActivity(addCodeIntent);
    }

    public void resetQRDismissCode(View view) {
        mPreferences.setDismissAlarmCode(SmartAlarmApplication.DEFAULT_DISMISS_ALARM_CODE);
        Toast.makeText(this,
                        getString(R.string.default_code_added) + " \"" + SmartAlarmApplication.DEFAULT_DISMISS_ALARM_CODE + "\"",
                        Toast.LENGTH_LONG)
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == WRITE_GALLERY_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                insertCodeImage();
            } else {
                Toast.makeText(this,
                        getString(R.string.code_not_added_to_gallery),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}