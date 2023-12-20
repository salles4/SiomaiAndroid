package com.salles.firstjavaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class Products extends AppCompatActivity {
    ArrayList<productsModel> productModel = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewDisplay);
        setUpProductModel();
        products_recycleAdapter adapter = new products_recycleAdapter(this, productModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    public void backOnClick(View view){
        super.onBackPressed();
        /*
        Intent intent = new Intent(this, LoggedInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        finish();
        startActivity(intent);*/
    }
    private void setUpProductModel(){
        for (int i = 0; i < 20; i++){
            int count = i;
            productModel.add(new productsModel("Product #",1,100));
        }
    }
}