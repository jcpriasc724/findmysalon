package com.findmysalon.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.findmysalon.R;

public class LoginActivity extends AppCompatActivity {

    Button btnSignin;
    Button btnSignup;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();

        btnSignin = (Button) findViewById(R.id.btn_signIn);
        btnSignup = (Button) findViewById(R.id.btn_signUp);
        email = findViewById(R.id.etx_email);
        password = findViewById(R.id.etx_password);


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.nav_type_user);
                Intent intent = new Intent(LoginActivity.this, CustomerActivity.class);
                //ide .putExtra("hi", "HI");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                //ide .putExtra("hi", "HI");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}