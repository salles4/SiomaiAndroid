package com.salles.firstjavaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Products extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
    }
    public void backOnClick(View view){
        Intent intent = new Intent(this, LoggedInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        finish();
        startActivity(intent);
    }
}