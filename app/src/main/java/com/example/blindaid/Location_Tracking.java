package com.example.blindaid;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;
import androidx.core.view.GestureDetectorCompat;
import android.view.GestureDetector;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;

public class Location_Tracking extends AppCompatActivity {

    FirebaseFirestore db;
    private CollectionReference busData;

    String Bus_Number;

    private FusedLocationProviderClient fusedLocationClient;
    Double current_latitude, current_longitude;
    float distance;
    float[]results = new float[1];

    DecimalFormat numberformat;

    GestureDetectorCompat mGestureDetector;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__tracking);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        current_location();
        numberformat = new DecimalFormat("#0" +
                ".0");

        Intent intent = getIntent();
        Bus_Number = intent.getStringExtra("Bus_number");

        db = FirebaseFirestore.getInstance();
        busData = db.collection("BusData");

        mGestureDetector = new GestureDetectorCompat(Location_Tracking.this, new GestureListeners());

    }

    private void current_location() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            current_latitude  = location.getLatitude();
                            current_longitude = location.getLongitude();

                        }
                    }
                });
    }

    private class GestureListeners extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            location_display();
            return super.onSingleTapConfirmed(e);
        }
    }

    private void location_display() {
        busData.whereEqualTo("bus_number", Bus_Number ).
                get().
                addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            if(note.getLatitude() == 0 && note.getLongitude() == 0){
                                MainActivity.getInstance().speak("आपके बस ने अभी तक अपने मूल स्थान से प्रस्थान नहीं किया है");
                            }
                            else {
                                Location.distanceBetween(current_latitude, current_longitude, note.getLatitude(), note.getLongitude(), results);
                                distance = (float) (results[0] / 1000);
                                MainActivity.getInstance().speak("\n" +
                                        "आपकी बस " + numberformat.format(distance) + "किलोमीटर दूर है");

                            }
                        }
                    }
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}

