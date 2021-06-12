package com.app.appvault;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.github.omadahealth.lollipin.lib.managers.LockManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.app.appvault.util.AppData.questions1;
import static com.app.appvault.util.AppData.questions2;
import static com.app.appvault.util.AppData.questions3;

public class ForgotPasswordActivity extends AppCompatActivity {
    Intent lockIntent;
    LockManager<CustomPinActivity> lockManager;
    private final int REQUEST_FIRST_RUN_PIN = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        lockManager = LockManager.getInstance();

        lockManager.enableAppLock(this, CustomPinActivity.class);
        lockManager.getAppLock().setLogoId(R.drawable.pin);

        lockIntent = new Intent(this, CustomPinActivity.class);

        String data = readFromFile(this);
        String[] split = data.split(":");
        String q1 = questions1[Integer.parseInt(split[0].split("-")[0])];

        String q2 = questions2[Integer.parseInt(split[1].split("-")[0])];

        String q3 = questions3[Integer.parseInt(split[2].split("-")[0])];

        ((TextView) findViewById(R.id.txtQ1)).setText(q1);
        ((TextView) findViewById(R.id.txtQ2)).setText(q2);
        ((TextView) findViewById(R.id.txtQ3)).setText(q3);
    }


    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;

    }

    public void btnReset(View view) {
        String data = readFromFile(this);
        String[] split = data.split(":");
        String q1 = questions1[Integer.parseInt(split[0].split("-")[0])];
        String a1 = split[0].split("-")[1];
        String q2 = questions2[Integer.parseInt(split[1].split("-")[0])];
        String a2 = split[1].split("-")[1];
        String q3 = questions3[Integer.parseInt(split[2].split("-")[0])];
        String a3 = split[2].split("-")[1];


        String ans1 = ((EditText) findViewById(R.id.txtAns1)).getText().toString();
        String ans2 = ((EditText) findViewById(R.id.txtAns2)).getText().toString();
        String ans3 = ((EditText) findViewById(R.id.txtAns3)).getText().toString();

        if (a1.equalsIgnoreCase(ans1) && a2.equalsIgnoreCase(ans2) && a3.equalsIgnoreCase(ans3)) {
            lockManager.getAppLock().setPasscode(null);
            lockIntent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
            startActivityForResult(lockIntent,REQUEST_FIRST_RUN_PIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case REQUEST_FIRST_RUN_PIN:
                    startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                    break;
            }
    }
}
