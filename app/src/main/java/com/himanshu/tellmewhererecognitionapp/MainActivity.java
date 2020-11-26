package com.himanshu.tellmewhererecognitionapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private static final int SPEAK_REQUEST = 10;

    TextView txtValue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtValue = findViewById(R.id.txtValue);

        PackageManager packageManager = this.getPackageManager();

        List<ResolveInfo> listOfInformation = packageManager.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH) , 0);

        if(listOfInformation.size() > 0)
        {
            Toast.makeText(MainActivity.this , "Your Device Supports Speech Recognition" , Toast.LENGTH_SHORT).show();
            listenToTheUserVoice();
        }
        else
        {
            Toast.makeText(MainActivity.this , "Your Device dosen't support Speech Recognition" , Toast.LENGTH_SHORT).show();
        }
    }


    private void listenToTheUserVoice()
    {
        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT , "Talk To Me");
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL , RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // such that user can speak in any language.
        voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS , 10);      //max results returned =10

        startActivityForResult(voiceIntent , SPEAK_REQUEST);    // as we want the results to be back from the intent so ForResults used.
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SPEAK_REQUEST && resultCode == RESULT_OK)
        {
            ArrayList<String> voiceWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            float[] confidLevels = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);

            int index = 0;
            for(String userWord : voiceWords)
            {
                if(confidLevels != null && index < confidLevels.length)
                {
                    txtValue.setText(userWord + " - " + confidLevels[index]);
                }
            }
        }
    }
}