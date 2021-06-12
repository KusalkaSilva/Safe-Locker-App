package com.app.appvault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.github.omadahealth.lollipin.lib.managers.LockManager;

public class SettingsActivity extends AppCompatActivity {
    Intent lockIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();

        lockManager.enableAppLock(this, CustomPinActivity.class);
        lockManager.getAppLock().setLogoId(R.drawable.pin);
        lockIntent = new Intent(this, CustomPinActivity.class);
    }

    public void onChangeQuestions(View view) {
        startActivity(new Intent(this, QuestionActivity.class));
    }

    public void onChangePIn(View view) {
        lockIntent.putExtra(AppLock.EXTRA_TYPE, AppLock.CHANGE_PIN);
        startActivity(lockIntent);
    }
}
