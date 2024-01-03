package com.salles.siomai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class RevenueActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference revenueRef = db.collection("revenue");
    NumberFormat pesoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
    TextView totalRevenueView;
    ProgressBar loading;
    int totalRevenue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        totalRevenueView = findViewById(R.id.totalRevenueView);
        loading = findViewById(R.id.revenueLoading);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //setups snapshot listener
        //updates list if database is updated
        revenueRef.orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(RevenueActivity.this, "Cannot load data.", Toast.LENGTH_SHORT).show();
                    Log.d("RevenueActivity", error.toString());
                    return;
                }
                LinearLayout list = findViewById(R.id.salesList);
                list.removeAllViews();
                loading.setVisibility(ProgressBar.VISIBLE);
                Handler handler = new Handler();
                int delayInMillis = 100;
                int delay = 0;
                totalRevenue = 0;
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Revenue revenue = documentSnapshot.toObject(Revenue.class);
                            revenue.setDocumentId(documentSnapshot.getId());
                            addRevenueItem(revenue);
                            totalRevenue += revenue.getRevenue();
                            totalRevenueView.setText(pesoFormat.format(totalRevenue));
                        }
                    },delay);
                    delay += delayInMillis;
                }
                loading.setVisibility(ProgressBar.GONE);

            }
        });
    }

    TextInputEditText revDialogEditText, selDialogEditText, changeDialogEditText;
    EditText cutDialogEditText;
    @SuppressLint("ClickableViewAccessibility")
    public void addClicked(View view){
        //setting up custom dialog layout
        View viewInf = getLayoutInflater().inflate(R.layout.revenue_add_dialog, null);

        //set current date to date field
        Calendar calendar = Calendar.getInstance();
        TextInputEditText dateInput = viewInf.findViewById(R.id.dateRevenueInput);
        dateInput.setText(DateFormat.format("EEE - MM/dd/yyyy", calendar.getTime()));

        //date picker
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int m,d,y; // stores current date
                m = calendar.get(Calendar.MONTH);
                d = calendar.get(Calendar.DAY_OF_MONTH);
                y = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(RevenueActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth,0,0,0);
                        dateInput.setText(DateFormat.format("EEE - MM/dd/yyyy", calendar.getTime()));
                    }
                }, y,m,d);
                datePickerDialog.show();
            }
        });

        //initializing editTexts and views to be auto update
        revDialogEditText = viewInf.findViewById(R.id.revenueEditText);
        selDialogEditText = viewInf.findViewById(R.id.sellerEditText);
        changeDialogEditText = viewInf.findViewById(R.id.changeEditText);
        cutDialogEditText = viewInf.findViewById(R.id.cutEditText);
        totalView = viewInf.findViewById(R.id.totalRevenueDialogView);
        cutAmountView = viewInf.findViewById(R.id.cutAmountView);
        cutCheckbox = viewInf.findViewById(R.id.cutCheckBox);

        revDialogEditText.addTextChangedListener(addDialogWatcher);
        changeDialogEditText.addTextChangedListener(addDialogWatcher);
        cutDialogEditText.addTextChangedListener(addDialogWatcher);

        cutCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDialogViews();
            }
        });

        //adding suggestions to locationField
        AutoCompleteTextView loc = viewInf.findViewById(R.id.locationEditText);
        String[] suggestions = {"Villa Antonina","Holy Trinity Parish","Cuneta - INC", "Queens Row"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, suggestions);
        loc.setAdapter(adapter);

        // for immediately showing suggestions when focused
        loc.setOnTouchListener((View v, MotionEvent event) -> {
            if(!loc.isPopupShowing()) loc.showDropDown();
            return false;
        });

        //adding revenue data dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewInf)
                .setTitle("Add Revenue Data")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(RevenueActivity.this, "Pressed Cancel", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //creates new object for db
                        Revenue revenue = new Revenue(
                                new Timestamp(calendar.getTime()),
                                String.valueOf(loc.getText()),
                                Integer.parseInt(String.valueOf(revDialogEditText.getText())),
                                String.valueOf(selDialogEditText.getText()),
                                0,false
                        );
                        // add object to db
                        revenueRef.add(revenue).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(RevenueActivity.this, "Data has been added successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RevenueActivity.this, "Failed to add the data. Please retry later.\n"+e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).show();

    }
    TextView totalView, cutAmountView;
    CheckBox cutCheckbox;
    private void updateDialogViews(){
        int revAmt = getIntView(revDialogEditText);
        int changeAmt = getIntView(changeDialogEditText);
        double cutAmt = getIntView(cutDialogEditText);
        double revenueCut = (revAmt - changeAmt) * (cutAmt/100);
        double finalRev = revAmt - revenueCut - changeAmt;

        if(cutCheckbox.isChecked()){
            cutDialogEditText.setEnabled(true);

            cutAmountView.setText(pesoFormat.format(revenueCut));
            totalView.setText(pesoFormat.format(finalRev));
        }else{
            cutDialogEditText.setEnabled(false);
            finalRev = revAmt - changeAmt;

            cutAmountView.setText("P - - - -");
            totalView.setText(pesoFormat.format(finalRev));
        }
    }

    private final TextWatcher addDialogWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateDialogViews();
        }
        @Override
        public void afterTextChanged(Editable s) {}
    };

    public int getIntView(TextInputEditText editText){
        String val = String.valueOf(editText.getText());
        if (val.equals("")){
            return 0;
        }else {
            return Integer.parseInt(val);
        }
    }
    public int getIntView(EditText editText){
        String val = String.valueOf(editText.getText());
        if (val.equals("")){
            return 0;
        }else {
            return Integer.parseInt(val);
        }
    }
    public void addRevenueItem(Revenue revenue){
        //creates a view to store revenue item template
        @SuppressLint("InflateParams")
        View views = getLayoutInflater().inflate(R.layout.revenue_card, null);

        TextView loc, rev, date;
        loc = views.findViewById(R.id.locationView);
        rev = views.findViewById(R.id.revenueView);
        date = views.findViewById(R.id.dateView);

        loc.setText(revenue.getLocation());
        rev.setText(pesoFormat.format(revenue.getRevenue()));
        date.setText(revenue.getConvertedDate());

        LinearLayout list = findViewById(R.id.salesList);
        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        views.startAnimation(animation);
        list.addView(views);
    }

    public void backOnClick(View view){
        getOnBackPressedDispatcher().onBackPressed();
    }

}

class Revenue {
    private String location="", seller="", documentId;
    private Timestamp date;
    private int revenue=0, change=0;
    private boolean cut25 = false;
    public Revenue(){
        //public no arguments method for firestore
    }

    public Revenue(Timestamp date, String location, int revenue, String seller, int change, boolean cut25) {
        this.location = location;
        this.seller = seller;
        this.date = date;
        this.revenue = revenue;
        this.change = change;
        this.cut25 = cut25;
    }

    public String getLocation() {
        return location;
    }

    public String getSeller() {
        return seller;
    }

    public Timestamp getDate() {
        return date;
    }

    @Exclude
    public String getConvertedDate(){
        return (String) DateFormat.format("EEE\nMM/dd", date.toDate());
    }

    public int getRevenue() {
        return revenue;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getChange() {
        return change;
    }

    public boolean isCut25() {
        return cut25;
    }
}