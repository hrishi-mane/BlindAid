package com.example.blindaidbusdriver;

import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.app.PendingIntent;

import android.content.Intent;


import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Spinner busSelector;
    ArrayList<String> list;
    ArrayAdapter<String> myAdapter;

    String bus_number;
    String document_id;

    FirebaseFirestore db;
    private CollectionReference busData;

    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    static  MainActivity instance;

    //Create Button btn
    Button btn;

    public  static MainActivity getInstance(){
        return  instance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button)findViewById(R.id.button);

        db = FirebaseFirestore.getInstance();
        busData = db.collection("BusData");

        busSelector = (Spinner) findViewById(R.id.spinner);
        list = new ArrayList<String>();

        createDropDownMenu();

        instance = this;

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        updateLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "You must grant this permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

        update_bus_location_fetch();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> addData = new HashMap<>();
                addData.put("latitude", 0);
                addData.put("longitude", 0);
                busData.document(document_id).set(addData,SetOptions.merge());
                fusedLocationProviderClient.removeLocationUpdates(getPendingIntent());
            }
        });
    }

    private void updateLocation() {
       locationBuilder();
       fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    private PendingIntent getPendingIntent() {

        Intent intent = new Intent(MainActivity.this, LocationService.class);
        intent.setAction(LocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void locationBuilder() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(0f);
    }

    private void createDropDownMenu() {
        list.add("Select the Bus Number");
        busData.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String bus_no = documentSnapshot.getString("bus_number");
                    list.add(bus_no);
                }
            }
        });
        myAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, list);
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        busSelector.setAdapter(myAdapter);
    }


    private void update_bus_location_fetch() {
        busSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    bus_number = parent.getItemAtPosition(position).toString();
                    busData.whereEqualTo("bus_number", bus_number).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                document_id = documentSnapshot.getId();
                            }
                        }
                    });
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest,getPendingIntent());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void updateFireStore(double latitude, double longitude) {
        Map<String, Object> addData = new HashMap<>();
        addData.put("latitude", latitude);
        addData.put("longitude", longitude);
        busData.document(document_id).set(addData,SetOptions.merge());
    }
}
