package com.app.appvault;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.app.appvault.services.APictureCapturingService;
import com.app.appvault.services.PictureCapturingServiceImpl;
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity;
import com.github.omadahealth.lollipin.lib.managers.LockManager;

import java.util.TreeMap;
import java.util.function.BiConsumer;


public class CustomPinActivity extends AppLockActivity implements PictureCapturingListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private APictureCapturingService pictureService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LockManager lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, MainActivity.class);
        lockManager.getAppLock().setShouldShowForgot(true);
        pictureService = PictureCapturingServiceImpl.getInstance(this);

    }

    @Override
    public void showForgotDialog() {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    @Override
    public void onPinFailure(int attempts) {
        if(attempts>=3) {
            pictureService.startCapturing(CustomPinActivity.this);
        }
    }

    @Override
    public void onPinSuccess(int attempts) {

    }


    @Override
    public int getContentView() {
        return R.layout.pin_activity;
    }

    @Override
    public void onDoneCapturingAllPhotos(TreeMap<String, byte[]> picturesTaken) {
        if (picturesTaken != null && !picturesTaken.isEmpty()) {
            picturesTaken.forEach(new BiConsumer<String, byte[]>() {
                @Override
                public void accept(String pictureUrl, byte[] pictureData) {
                    //convert the byte array 'pictureData' to a bitmap (no need to read the file from the external storage) but in case you
                    //You can also use 'pictureUrl' which stores the picture's location on the device
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);
                }
            });
//            showToast("Done capturing all photos!");
            return;
        }
        showToast("No camera detected!");
    }

    @Override
    public void onCaptureDone(String pictureUrl, final byte[] pictureData) {
        if (pictureData != null && pictureUrl != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    final Bitmap bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);

                    final int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                    final Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

                }
            });
            showToast("Intruder Captured");
        }
    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              Toast.makeText(CustomPinActivity.this.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                          }
                      }
        );
    }
}
