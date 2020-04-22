package com.example.blindaid;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText source;
    EditText destination;

    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;
    private TextToSpeech myTTS;

    int click_count;
    String bus_number;

    FirebaseFirestore db;
    private CollectionReference busData;

    StringBuffer buffer;

    Date dt2;


    GestureDetectorCompat mGestureDetector;

    static MainActivity instance;

    public static MainActivity getInstance(){
        return instance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        audioPermission();
        initializeTextToSpeech();

        source = (EditText) findViewById(R.id.editText);
        destination = (EditText) findViewById(R.id.editText5);

        db = FirebaseFirestore.getInstance();
        busData = db.collection("BusData");

        dt2 = new Date();
        buffer = new StringBuffer();

        click_count = 0;

        mGestureDetector = new GestureDetectorCompat(MainActivity.this, new GestureListener());

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());



        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
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
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null & click_count == 2) {
                    source.setText(matches.get(0));
                    speak("Source location दर्ज किया गया है" + source.getText().toString() + ", Destination बोलने के लिए फिर से क्लिक करें");
                }
                else {
                    destination.setText(matches.get(0));
                    speak("Destination दर्ज किया गया है" + destination.getText().toString());
                }


            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }
    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public void onLongPress(MotionEvent e) {
            speak("Source और Destination हटाएँ गए हैं। कृपया फिर से दर्ज करें");
            click_count = 0;
            source.setText("");
            destination.setText("");
            super.onLongPress(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Intent intent = new Intent(MainActivity.this, Location_Tracking.class);
            intent.putExtra("Bus_number",bus_number);
            startActivity(intent);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            click_count = click_count + 1;
                if (click_count == 1) {
                    speak("आपने Source चुना है। बोलने के लिए फिर से क्लिक करें");
                }
                if (click_count == 2) {
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

                }
                if (click_count == 3) {
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                }

                if (click_count >= 4) {
                    loadNote();

                }
            return super.onSingleTapConfirmed(e);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void loadNote() {
        buffer.delete(0, buffer.length());
        busData.whereEqualTo("source", source.getText().toString())
                .whereEqualTo("destination", destination.getText().toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            bus_number = note.getBus_number();
                            Calendar stored_date = dateToCalendar(note.getDeparture_time());
                            Calendar current_date = dateToCalendar(dt2);

                            if(stored_date.get(Calendar.HOUR_OF_DAY) > current_date.get(Calendar.HOUR_OF_DAY) ||
                                    (stored_date.get(Calendar.HOUR_OF_DAY) == current_date.get(Calendar.HOUR_OF_DAY) &&
                                    stored_date.get(Calendar.MINUTE) > current_date.get(Calendar.MINUTE)) ) {

                                buffer.append("इस रूट के लिए बस नंबर ").append(note.getBus_number()).append("है");
                                buffer.append("\n");

                                if(dateToCalendar(note.getDeparture_time()).get(Calendar.HOUR_OF_DAY) < 10 &&
                                        dateToCalendar(note.getDeparture_time()).get(Calendar.MINUTE) < 10){

                                    buffer.append("बस" + "0").append(dateToCalendar(note.getDeparture_time()).get(Calendar.HOUR_OF_DAY)).
                                            append(":").append("0").append(dateToCalendar(note.getDeparture_time()).get(Calendar.MINUTE)).
                                            append(note.getSource()).append("से रवाना होगी ");
                                }
                                else if(dateToCalendar(note.getDeparture_time()).get(Calendar.HOUR_OF_DAY) < 10 &&
                                        dateToCalendar(note.getDeparture_time()).get(Calendar.MINUTE) > 10){

                                    buffer.append("बस" + "0").append(dateToCalendar(note.getDeparture_time()).get(Calendar.HOUR_OF_DAY)).append(":").
                                            append(dateToCalendar(note.getDeparture_time()).get(Calendar.MINUTE)).append(note.getSource()).
                                            append("से रवाना होगी ");
                                }
                                else if(dateToCalendar(note.getDeparture_time()).get(Calendar.HOUR_OF_DAY) > 10 &&
                                        dateToCalendar(note.getDeparture_time()).get(Calendar.MINUTE) < 10){

                                    buffer.append("बस").append(dateToCalendar(note.getDeparture_time()).get(Calendar.HOUR_OF_DAY)).append(":").
                                            append("0").append(dateToCalendar(note.getDeparture_time()).get(Calendar.MINUTE)).append(note.getSource()).
                                            append("से रवाना होगी ");
                                }
                                else{
                                    buffer.append("बस").append(dateToCalendar(note.getDeparture_time()).get(Calendar.HOUR_OF_DAY)).append(":").
                                            append(dateToCalendar(note.getDeparture_time()).get(Calendar.MINUTE)).append(note.getSource()).
                                            append("से रवाना होगी ");
                                }
                                buffer.append("\n");


                                if(dateToCalendar(note.getArrival_time()).get(Calendar.HOUR_OF_DAY) < 10 &&
                                        dateToCalendar(note.getArrival_time()).get(Calendar.MINUTE) < 10){

                                    buffer.append("और" + "0").append(dateToCalendar(note.getArrival_time()).get(Calendar.HOUR_OF_DAY)).append(":").
                                            append("0").append(dateToCalendar(note.getDeparture_time()).get(Calendar.MINUTE)).append("पे").
                                            append(note.getDestination()).append("पहुंचेगी");
                                }
                                else if(dateToCalendar(note.getArrival_time()).get(Calendar.HOUR_OF_DAY) < 10 &&
                                        dateToCalendar(note.getArrival_time()).get(Calendar.MINUTE) > 10){

                                    buffer.append("और" + "0").append(dateToCalendar(note.getArrival_time()).get(Calendar.HOUR_OF_DAY)).append(":").
                                            append(dateToCalendar(note.getArrival_time()).get(Calendar.MINUTE)).append("पे").append(note.getDestination()).
                                            append("पहुंचेगी");
                                }

                                else if(dateToCalendar(note.getArrival_time()).get(Calendar.HOUR_OF_DAY) > 10 &&
                                        dateToCalendar(note.getArrival_time()).get(Calendar.MINUTE) < 10){

                                    buffer.append("और").append(dateToCalendar(note.getArrival_time()).get(Calendar.HOUR_OF_DAY)).append(":").append("0").
                                            append(dateToCalendar(note.getArrival_time()).get(Calendar.MINUTE)).append("पे").append(note.getDestination()).
                                            append("पहुंचेगी");
                                }
                                else{
                                    buffer.append("और").append(dateToCalendar(note.getArrival_time()).get(Calendar.HOUR_OF_DAY)).append(":").
                                            append(dateToCalendar(note.getArrival_time()).get(Calendar.MINUTE)).append("पे").append(note.getDestination()).
                                            append("पहुंचेगी");
                                }

                                buffer.append("\n").append("बस का कुल यात्रा समय").append(note.getTravel_time()).append("मिनट है").append("\n").append("\n");
                            }
                        }
                        speak(buffer.toString());

                        if(buffer.toString().length() == 0){
                            speak("आज इस मार्ग के लिए कोई बस उपलब्ध नहीं है");
                        }
                    }
                });

    }


    private Calendar dateToCalendar(Date dt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        return calendar;
    }


    //method to initialize the TTS object and to check the availability of TTS engine
    private void initializeTextToSpeech() {
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int i) {
                if (myTTS.getEngines().size() == 0) {
                    Toast.makeText(MainActivity.this, "There is no TTS engine on your device",
                            Toast.LENGTH_LONG).show();
                } else {

                    myTTS.setLanguage(Locale.forLanguageTag("hin"));
                    speak("आपने बस मार्गदर्शन सुविधा खोली है");

                }
            }
        });
    }

    //method for implementing background voice
    public void speak(String s) {
        if (Build.VERSION.SDK_INT >= 21) {
            myTTS.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            myTTS.speak(s, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    //method to shutdown the tts engine after use
    @Override
    protected void onPause() {
        super.onPause();
    }

    /*method is used to open the setting menu upon opening the app to grant
    the microphone permission to the application*/

    private void audioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat
                    .checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();

            }
        }
    }
}
