package com.findmysalon.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.findmysalon.R;

public class TypeUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_user);
    }

    @Override
    protected void onStart() {
        super.onStart();

        CardView cardBusiness = (CardView) findViewById(R.id.business_card);
        CardView cardCustomer = (CardView) findViewById(R.id.customer_card);

        cardBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.nav_reg_business);

                Intent intent = new Intent(TypeUserActivity.this, RegistrationActivity.class);
                //ide .putExtra("hi", "HI");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        cardCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}