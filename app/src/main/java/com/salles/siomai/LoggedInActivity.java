package com.salles.siomai;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoggedInActivity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void LogOut(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log Out?")
                .setMessage("Are you sure you want to log out?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(LoggedInActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();


    }
    public void productsOnClick(View view){
        Intent intent = new Intent(this, ProductsActivity.class);
        startActivity(intent);
    }
    public void revenueOnClick(View view){
        Intent intent = new Intent(this, RevenueActivity.class);
        startActivity(intent);
    }
    public void expensesOnClick(View view){
        Intent intent = new Intent(this, ExpensesActivity.class);
        startActivity(intent);
    }
    public void websiteOnClick(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://salles4.github.io/SiomaiYan/soon.html"));
        startActivity(intent);
    }
    public void supportOnClick(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.me/ciss.04"));
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
