package com.example.blindaid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class User_Emergency extends AppCompatActivity{
    EditText Name;
    EditText Phone_no;
    Button AddContact;
    boolean contact_inserted;
    Database myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__emergency);
        myDb = new Database(this);

        Name = findViewById(R.id.name);
        Phone_no = findViewById(R.id.phoneno);
        AddContact = findViewById(R.id.add);


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

    }
}