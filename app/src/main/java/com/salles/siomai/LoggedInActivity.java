package com.salles.siomai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoggedInActivity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void LogOut(View view){

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        finish();
        startActivity(intent);
    }
    public void productsOnClick(View view){
        Intent intent = new Intent(this, ProductsActivity.class);
        startActivity(intent);
    }
    public void salesOnClick(View view){
        Intent intent = new Intent(this, SalesActivity.class);
        startActivity(intent);
    }
    boolean mExitConfirmation = false;
    @Override
    public void onBackPressed() {
        if (mExitConfirmation) {
            super.onBackPressed();
            finish();
        } else {
            mExitConfirmation = true;
            Handler handler = new Handler();
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
            handler.postDelayed(() -> mExitConfirmation = false, 2000); // Reset flag after 2 seconds
        }
    }
}
