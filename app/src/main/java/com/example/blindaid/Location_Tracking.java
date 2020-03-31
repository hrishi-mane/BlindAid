package com.example.blindaid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class Location_Tracking extends AppCompatActivity {

    FirebaseFirestore db;
    private CollectionReference busData;

    String Bus_Number;

    GestureDetectorCompat mGestureDetector;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__tracking);
        Intent intent = getIntent();
        Bus_Number = intent.getStringExtra("Bus_number");

        db = FirebaseFirestore.getInstance();
        busData = db.collection("BusData");

        mGestureDetector = new GestureDetectorCompat(Location_Tracking.this, new GestureListeners());
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
                            String latitude = note.getLatitude();
                            String longitude = note.getLongitude();
                            Toast.makeText(Location_Tracking.this, "latitude:" + latitude + "longitude:" + longitude, Toast.LENGTH_SHORT).show();
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
