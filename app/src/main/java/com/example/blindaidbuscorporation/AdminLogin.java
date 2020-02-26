package com.example.blindaidbuscorporation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLogin extends AppCompatActivity {

    private EditText usrname ;
    private EditText passwd ;
    private Button submit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        usrname = (EditText) findViewById(R.id.username);
        passwd = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.button4);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwd.getText().toString().trim().equalsIgnoreCase("12345") & usrname.getText().toString().trim().equalsIgnoreCase("Admin"))
                {
                    Intent intent = new Intent(AdminLogin.this,MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(AdminLogin.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
