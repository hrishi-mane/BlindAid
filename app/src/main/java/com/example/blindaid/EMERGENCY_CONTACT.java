package com.example.blindaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EMERGENCY_CONTACT extends AppCompatActivity {

    EditText phone_number;
    Button btn1;

    private TextToSpeech myTTS;
    private SpeechRecognizer mySpeechRecognizer;

    private FusedLocationProviderClient fusedLocationClient;

    Double latitude, longitude;
    String lati,longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency__contact);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        


        check_sms_permission();
        check_location_permission();

        phone_number = (EditText)findViewById((R.id.editText2));
        btn1 = (Button)findViewById(R.id.button7);

        initializeTextToSpeech();
        initializeSpeechRecognizer();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                mySpeechRecognizer.startListening(intent);

            }
        });
    }

    private void check_location_permission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(EMERGENCY_CONTACT.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EMERGENCY_CONTACT.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(EMERGENCY_CONTACT.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(EMERGENCY_CONTACT.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
        }

        else {
            // Permission has already been granted
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                lati  = String.valueOf(latitude);
                                longi = String.valueOf(longitude);

                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
            else{

            }

        }
    }

    private void initializeSpeechRecognizer() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> reslt = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    processResult(reslt.get(0));
                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }
    }





    private void processResult(String command) {

        command = command.toLowerCase();


        if(command.indexOf("message") != -1) {
            if (command.indexOf("pushkar") != -1) {
                speak("Sending sms to pushkar");
                myMessage("8692974748","This is an emergency message.I am lost");
                myMessage2("8692974748", lati + longi);
            }
        }

    }

    private void initializeTextToSpeech() {

        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(myTTS.getEngines().size()==0) {
                    Toast.makeText(EMERGENCY_CONTACT.this,"There is no TTS engine on your device",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    myTTS.setLanguage(Locale.US);
                    speak("You have opened emergency contact Pushkar Kaswankar is registered as your emergency contacts. Tap the screen to speak the name.");
                }
            }
        });
    }




    private void speak(String s) {

        if(Build.VERSION.SDK_INT >= 21) {
            myTTS.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            myTTS.speak(s, TextToSpeech.QUEUE_FLUSH, null);
        }
    }




    @Override
    protected void onPause() {
        super.onPause();
    }

    private void check_sms_permission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(!(ContextCompat
                    .checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED)){
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName() ));
                startActivity(intent);
                finish();

            }
        }
    }




    private void myMessage(String phone, String message) {

        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone,null,message,null,null);
            Toast.makeText(this ,"Message sent" , Toast.LENGTH_SHORT).show();

        }

        catch (Exception e){
            Toast.makeText(this ,"Message failed" , Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void myMessage2(String phone, String message) {

        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone,null,message,null,null);
            Toast.makeText(this ,"Message sent" , Toast.LENGTH_SHORT).show();

        }

        catch (Exception e){
            Toast.makeText(this ,"Message failed" , Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

}
