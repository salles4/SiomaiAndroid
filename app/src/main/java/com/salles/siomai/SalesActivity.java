package com.salles.siomai;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class SalesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

    }
    public void addSales(View view){
        @SuppressLint("InflateParams")
        View views = getLayoutInflater().inflate(R.layout.sales_card, null);

        LinearLayout list = findViewById(R.id.salesList);
        list.addView(views, 0);
    }
    public void backOnClick(View view){
        super.onBackPressed();
    }
}