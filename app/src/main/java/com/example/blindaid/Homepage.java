package com.example.blindaid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Homepage extends AppCompatActivity {
    Button bus;
    Button message;
//    Button admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        bus = (Button)findViewById(R.id.img_button_1);
        message = (Button)findViewById(R.id.img_button_2);
//        admin = (Button) findViewById(R.id.button3);

//        admin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                activity3();
//            }
//        });

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
    }


    private void activity1() {
        Intent intent = new Intent(Homepage.this,MainActivity.class);
        startActivity(intent);
    }
    private void activity2() {
        Intent intent = new Intent(this,EMERGENCY_CONTACT.class);
        startActivity(intent);
    }

//    private void activity3() {
//        Intent intent = new Intent(Homepage.this,AdminLogin.class);
//        startActivity(intent);
//    }



}
