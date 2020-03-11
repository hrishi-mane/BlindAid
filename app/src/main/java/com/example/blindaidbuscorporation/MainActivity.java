package com.example.blindaidbuscorporation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;


public class MainActivity extends AppCompatActivity {


    public static EditText Source, Destination, BusNo, BusTime, ArrivalTime, DepartureTime;
    Button Add;
//  Button Update;   //To be programmed in future
//  Button Delete;    // To be programmed in future
    Button Retrieve;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference busData = db.collection("BusData");
    StringBuffer buffer = new StringBuffer();

    @SuppressLint("SimpleDateFormat")
    DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    String date;

    TimePickerDialog timePickerDialog;
    Calendar calender = Calendar.getInstance();
    int currentHour;
    int currentMinute;
    String amPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        date = new SimpleDateFormat("yyyy/MM/dd").format(new Date()) ;

        Source = findViewById(R.id.sourcetext);
        Destination = findViewById(R.id.destinationtext);
        BusNo = findViewById(R.id.busnonumber);
        BusTime = findViewById(R.id.bustimetime);
        ArrivalTime = findViewById(R.id.arrivaltimetime);
        DepartureTime = findViewById(R.id.departuretimetime);
        Add = findViewById(R.id.addData);
//      Update = findViewById(R.id.update); //To be programmed in future
//      Delete = findViewById(R.id.delete); //To be programmed in future
        Retrieve = findViewById(R.id.retrieve);

        DepartureTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentHour = calender.get(Calendar.HOUR_OF_DAY);
                currentMinute = calender.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        if (hourOfDay < 10 && minute < 10) {
                            DepartureTime.setText("0" + hourOfDay + ":" + "0" + minute + ":" + "00");
                        }
                        else if(hourOfDay < 10 && minute > 10) {
                            DepartureTime.setText("0" + hourOfDay + ":" + minute + ":" + "00");
                        }
                        else if(hourOfDay > 10 && minute < 10) {
                            DepartureTime.setText(hourOfDay + ":" + "0" + minute + ":" + "00");
                        }
                        else{
                            DepartureTime.setText(hourOfDay + ":"+ minute + ":" + "00");
                        }

                    }
                },currentHour,currentMinute, android.text.format.DateFormat.is24HourFormat(MainActivity.this));
                timePickerDialog.show();
            }
        });

        ArrivalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentHour = calender.get(Calendar.HOUR_OF_DAY);
                currentMinute = calender.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay < 10 && minute < 10) {
                            ArrivalTime.setText("0" + hourOfDay + ":" + "0" + minute + ":" + "00");
                        }
                        else if(hourOfDay < 10 && minute > 10) {
                            ArrivalTime.setText("0" + hourOfDay + ":" + minute + ":" + "00");
                        }
                        else if(hourOfDay > 10 && minute < 10) {
                            ArrivalTime.setText(hourOfDay + ":" + "0" + minute + ":" + "00");
                        }
                        else{
                            ArrivalTime.setText(hourOfDay + ":"+ minute + ":" + "00");
                        }
                    }
                },currentHour,currentMinute, android.text.format.DateFormat.is24HourFormat(MainActivity.this));
                timePickerDialog.show();
            }
        });


        Add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       saveNote();

                    }
                }
        );

        Retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNote();
            }
        });

    }


    private void saveNote(){
        try {
            String source = Source.getText().toString();
            String destination = Destination.getText().toString();
            String bus_number = BusNo.getText().toString();
            Date arrival_time = sdf.parse(date + " "+ ArrivalTime.getText().toString());
            Date departure_time = sdf.parse(date + " "+ DepartureTime.getText().toString());
            String travel_time = BusTime.getText().toString();

            Note note = new Note(source, destination, bus_number, arrival_time, departure_time, travel_time);
            busData.add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(MainActivity.this, "Data Added", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Data Failed to Add", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (ParseException e){
            //
        }
    }

    private void loadNote() {
        buffer.delete(0, buffer.length());
        busData.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Note note = documentSnapshot.toObject(Note.class);

                    buffer.append("Source: " + note.getSource());
                    buffer.append("\n");
                    buffer.append("Destination: " + note.getDestination());
                    buffer.append("\n");
                    buffer.append("Bus Number: " + note.getBus_number());
                    buffer.append("\n");
                    buffer.append("Departure Time: " + note.getDeparture_time());
                    buffer.append("\n");
                    buffer.append("Arrival Time: " + note.getArrival_time());
                    buffer.append("\n");
                    buffer.append("Total Travel Time: " + note.getTravel_time());
                    buffer.append("\n");
                    buffer.append("\n");
                }
                showMessage("Data", buffer.toString());
            }
        });
    }

    public void showMessage(String Title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.show();
    }

}



