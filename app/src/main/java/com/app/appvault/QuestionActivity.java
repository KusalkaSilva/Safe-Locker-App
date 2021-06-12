package com.app.appvault;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static com.app.appvault.util.AppData.questions1;
import static com.app.appvault.util.AppData.questions2;
import static com.app.appvault.util.AppData.questions3;

public class QuestionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinnerQ1;
    Spinner spinnerQ2;
    Spinner spinnerQ3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        ArrayAdapter spinnerStyleAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, questions1);
        spinnerStyleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ1 = (Spinner) findViewById(R.id.spinnerQ1);
        spinnerQ1.setOnItemSelectedListener(this);
        spinnerQ1.setAdapter(spinnerStyleAdapter);

        ArrayAdapter spinnerStyleAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, questions2);
        spinnerStyleAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ2 = (Spinner) findViewById(R.id.spinnerQ2);
        spinnerQ2.setOnItemSelectedListener(this);
        spinnerQ2.setAdapter(spinnerStyleAdapter2);

        ArrayAdapter spinnerStyleAdapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, questions3);
        spinnerStyleAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ3 = (Spinner) findViewById(R.id.spinnerQ3);
        spinnerQ3.setOnItemSelectedListener(this);
        spinnerQ3.setAdapter(spinnerStyleAdapter3);

        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA : "+ readFromFile(this));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void btnSaveOnClick(View view) {
        int q1 = spinnerQ1.getSelectedItemPosition();
        int q2 = spinnerQ2.getSelectedItemPosition();
        int q3=spinnerQ3.getSelectedItemPosition();

        String a1=((EditText) findViewById(R.id.txtAnswer1)).getText().toString();
        String a2=((EditText) findViewById(R.id.txtAnswer2)).getText().toString();
        String a3=((EditText) findViewById(R.id.txtAnswer3)).getText().toString();

        String text=q1+"-"+a1+":"+q2+"-"+a2+":"+q3+"-"+a3;

        writeToFile(text,this);

        startActivity(new Intent(this,MainActivity.class));
    }


    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;

    }
}
