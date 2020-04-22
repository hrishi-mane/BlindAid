package com.example.blindaid;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


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

    Database myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency__contact);
        myDb = new Database(this);

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

            if (ActivityCompat.shouldShowRequestPermissionRationale(EMERGENCY_CONTACT.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                ActivityCompat.requestPermissions(EMERGENCY_CONTACT.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            } else {
                ActivityCompat.requestPermissions(EMERGENCY_CONTACT.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
        }

        else {
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

        Cursor res1 = myDb.retrieveContact(command);
        if(res1.getCount()== 0)
        {
            Toast.makeText(EMERGENCY_CONTACT.this,"No data found",Toast.LENGTH_LONG).show();
            return;
        }
        else {
            res1.moveToFirst();
            String phno = res1.getString(1);
            speak("संदेश भेजा है");
            myMessage(phno, "This is an emergency message.I am lost");
            myMessage(phno, lati + longi);
        }

    }

    private void initializeTextToSpeech() {

        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int i) {
                if(myTTS.getEngines().size()==0) {
                    Toast.makeText(EMERGENCY_CONTACT.this,"There is no TTS engine on your device",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    myTTS.setLanguage(Locale.forLanguageTag("hin"));
                    speak("आपने आपातकालीन संपर्क सुविधा खोली है\n" +
                            "आप किसे संदेश भेजना चाहते हैं");
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

}
