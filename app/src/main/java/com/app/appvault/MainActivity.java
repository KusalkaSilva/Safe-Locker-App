package com.app.appvault;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.GridLayout;

import com.app.appvault.util.HideFile;
import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.github.omadahealth.lollipin.lib.managers.LockManager;


public class MainActivity extends AppCompatActivity {
    String mPermissionWriteData = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    String mPermissionReadData = Manifest.permission.READ_EXTERNAL_STORAGE;
    String mPermissionCamera = Manifest.permission.CAMERA;
    private final int REQUEST_FIRST_RUN_PIN = 11;
    Intent lockIntent;
    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Dashboard");
        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();

        lockManager.enableAppLock(this, CustomPinActivity.class);

        lockIntent = new Intent(this, CustomPinActivity.class);
        lockManager.getAppLock().setLogoId(R.drawable.pin);
        if (!LockManager.getInstance().getAppLock().isPasscodeSet()) {
            lockIntent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
            startActivityForResult(lockIntent, REQUEST_FIRST_RUN_PIN);
        } else {
            lockIntent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
            startActivity(lockIntent);
        }
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermissionCamera) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermissionCamera, Manifest.permission.CAMERA}, 2);
            }
            if (ActivityCompat.checkSelfPermission(this, mPermissionWriteData) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermissionWriteData, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
            if (ActivityCompat.checkSelfPermission(this, mPermissionReadData) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermissionReadData, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        gridTileEvents(mainGrid);
    }

    private void gridTileEvents(GridLayout mainGrid) {
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (finalI) {
                        case 0:
                            Intent intentImage = new Intent(MainActivity.this, ImageActivity.class);
                            startActivity(intentImage);
                            break;
                        case 1:
                            Intent intentVideo = new Intent(MainActivity.this, VideoActivity.class);
                            startActivity(intentVideo);
                            break;
                        case 2:
//                            lockIntent.putExtra(AppLock.EXTRA_TYPE, AppLock.CHANGE_PIN);
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                            break;
                        case 3:
                            Intent intentFaud = new Intent(MainActivity.this, FaudActivity.class);
                            startActivity(intentFaud);
                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case REQUEST_FIRST_RUN_PIN:
                    startActivity(new Intent(MainActivity.this, QuestionActivity.class));
                    break;
            }
    }
}