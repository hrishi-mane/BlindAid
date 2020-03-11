package com.example.blindaid;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class User_Emergency extends AppCompatActivity{
    EditText Name;
    EditText Phone_no;
    Button AddContact,DeleteContact, ViewContact;;
    boolean contact_inserted;
    Integer contact_deleted;
    Database myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__emergency);
        myDb = new Database(this);

        Name = findViewById(R.id.name);
        Phone_no = findViewById(R.id.phoneno);
        AddContact = findViewById(R.id.add);
        DeleteContact = findViewById(R.id.del);
        ViewContact = findViewById(R.id.view);


        AddContact.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Name.getText().length() > 0 & Phone_no.getText().length() > 0) {
                            contact_inserted = myDb.insertContact(Name.getText().toString(),
                                    Phone_no.getText().toString()) ;
                            Toast.makeText(User_Emergency.this, "Contact Inserted", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(User_Emergency.this, "All Fields are Mandatory", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        DeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Name.getText().length() > 0 & Phone_no.getText().length() > 0) {
                    contact_deleted = myDb.deleteContact(Name.getText().toString(),
                            Phone_no.getText().toString());
                    Toast.makeText(User_Emergency.this, "Contact Deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(User_Emergency.this, "Contact Not Deleted", Toast.LENGTH_LONG).show();

                }
            }
        });


        ViewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res2 = myDb.rtrContact();
                if (res2.getCount() == 0) {
                    showMessage("Error", "No Data Found");
                    return;
                }
                else {
                    StringBuffer buffer = new StringBuffer();
                    res2.moveToFirst();
                    do {
                        buffer.append("Full_Name: " + res2.getString(0) + "\n");
                        buffer.append("Phone_Number: " + res2.getString(1) + "\n");
                    } while (res2.moveToNext());


                    showMessage("Data", buffer.toString());
                }
            }
        });

    }

    public void showMessage(String Title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.show();
    }
}