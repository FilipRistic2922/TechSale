package com.metropolitan.techsale.payment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.metropolitan.techsale.R;

import java.util.Objects;
public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}