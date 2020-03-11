package com.example.blindaid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Homepage extends AppCompatActivity {
    Button bus,add_contact;
    Button message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        bus = (Button)findViewById(R.id.img_button_1);
        message = (Button)findViewById(R.id.img_button_2);
        add_contact = (Button)findViewById(R.id.btn_add);


        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity1();
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity2();
            }
        });
        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity3();
            }
        });
    }


    private void activity1() {
        Intent intent = new Intent(Homepage.this,MainActivity.class);
        startActivity(intent);
    }

    private void activity2() {
        Intent intent = new Intent(this,EMERGENCY_CONTACT.class);
        startActivity(intent);
    }

    private void activity3() {
        Intent intent = new Intent(this, User_Emergency.class);
        startActivity(intent);
    }





}
