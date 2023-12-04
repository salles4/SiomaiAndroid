package com.salles.firstjavaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void LoginClicked(View view){
        EditText userText = findViewById(R.id.usernameField);
        EditText passText = findViewById(R.id.passwordField);
        String user = userText.getText().toString();
        String pass = passText.getText().toString();


        if(user.equals("salles")&&pass.equals("123")){
            Intent intent = new Intent(this, LoggedInActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }else{
            Toast.makeText(this, "Wrong credentials!", Toast.LENGTH_LONG).show();
        }
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
