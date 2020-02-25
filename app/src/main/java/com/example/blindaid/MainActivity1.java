package com.example.blindaid;



import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MainActivity1 extends AppCompatActivity {
//    boolean isInserted;
//   boolean isUpdated;
    Database myDb;
    EditText Source, Destination, BusNo, BusTime, ArrivalTime, DepartureTime;
    Button Add;
    Button Update;
    Button Delete;
    Button Retrieve;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference busData = db.collection("BusData");
    StringBuffer buffer = new StringBuffer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        myDb = new Database(this);

        Source = findViewById(R.id.sourcetext);
        Destination = findViewById(R.id.destinationtext);
        BusNo = findViewById(R.id.busnonumber);
        BusTime = findViewById(R.id.bustimetime);
        ArrivalTime = findViewById(R.id.arrivaltimetime);
        DepartureTime = findViewById(R.id.departuretimetime);
        Add = findViewById(R.id.addData);
        Update = findViewById(R.id.update);
        Delete = findViewById(R.id.delete);
        Retrieve = findViewById(R.id.retrieve);
//        UpdateData();
//        DeleteData();
//        RetrieveData();

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


    private void saveNote() {
        String source =  Source.getText().toString();
        String destination = Destination.getText().toString();
        String bus_number = BusNo.getText().toString();
        String arrival_time = ArrivalTime.getText().toString();
        String departure_time = DepartureTime.getText().toString();
        String travel_time = BusTime.getText().toString();

        Note note = new Note(source,destination,bus_number,arrival_time,departure_time,travel_time);
        busData.add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(MainActivity1.this, "Data Added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity1.this, "Data Failed to Add", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadNote() {
        buffer.delete(0, buffer.length());
        busData.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
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
                showMessage("Data",buffer.toString());
            }
        });
    }



//    public void UpdateData() {
//        Update.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (Source.getText().length() > 0 & Destination.getText().length() > 0 & BusTime.getText().length() > 0 & ArrivalTime.getText().length() > 0 & DepartureTime.getText().length() > 0) {
//                            isUpdated = myDb.updateData(Source.getText().toString(),
//                                    Destination.getText().toString(),
//                                    BusNo.getText().toString(),
//                                    BusTime.getText().toString(),
//                                    ArrivalTime.getText().toString(),
//                                    DepartureTime.getText().toString());
//                            Toast.makeText(MainActivity1.this, "Data Updated", Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(MainActivity1.this, "All Fields are Mandatory", Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                }
//        );
//
//    }



//    public void DeleteData() {
//        Delete.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Integer deletedRows = myDb.deleteData(
//                                BusNo.getText().toString());
//
//                        if (deletedRows > 0)
//                            Toast.makeText(MainActivity1.this, "Data Deleted", Toast.LENGTH_LONG).show();
//                        else
//                            Toast.makeText(MainActivity1.this, "Data Not Deleted", Toast.LENGTH_LONG).show();
//
//                    }
//                }
//        );
//    }
//

//
    public void showMessage(String Title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.show();
    }
}

//    Cursor res = myDb.retrieveData(Source.getText().toString(),Destination.getText().toString());
//                        if(res.getCount() == 0){
//                            showMessage("Error","No Data Found");
//                            return;
//                        }
//                        else{
//                            StringBuffer buffer = new StringBuffer();
//                            res.moveToFirst();
//                            do {
//                                buffer.append("Bus No: " + res.getString(2) + "\n");
//                                buffer.append("Bus Time: " + res.getString(3) + "\n");
//                                buffer.append("Arrival Time: " + res.getString(4) + "\n");
//                                buffer.append("Departure Time: " + res.getString(5) + "\n\n");
//                            }while(res.moveToNext());
//
//
//                            showMessage("Data",buffer.toString());
//                        }

