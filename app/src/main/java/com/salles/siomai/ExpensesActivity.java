package com.salles.siomai;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExpensesActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference expenseRef = db.collection("expenses");
    NumberFormat pesoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
    private final String DATE_FORMAT = "EEE - MM/dd/yyyy";
    ProgressBar loadingExpense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        refreshList();
        loadingExpense = findViewById(R.id.loadingExpenses);
    }
    public void addExpenseClicked(View view){
        @SuppressLint("InflateParams")
        View viewInflator = getLayoutInflater().inflate(R.layout.expenses_add_dialog, null);

        Calendar calendar = Calendar.getInstance();
        TextInputEditText dateInput = viewInflator.findViewById(R.id.dateExpenseInput);
        dateInput.setText(DateFormat.format("EEE - MM/dd/yyyy", calendar.getTime()));
        dateInput.setOnClickListener(v -> {
            int m,d,y;
            m = calendar.get(Calendar.MONTH);
            d = calendar.get(Calendar.DAY_OF_MONTH);
            y = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(ExpensesActivity.this, (view1, year, month, dayOfMonth)->{
                calendar.set(year,month,dayOfMonth,0,0,0);
                dateInput.setText(DateFormat.format(DATE_FORMAT, calendar.getTime()));
            }, y,m,d);
            datePickerDialog.show();
        });

        TextInputEditText amt, desc;
        amt = viewInflator.findViewById(R.id.amtExpenseEditText);
        desc = viewInflator.findViewById(R.id.descExpenseEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewInflator).setTitle("Add Expense Data")
                .setNegativeButton("Cancel", (dialog, which) -> {})
                .setPositiveButton("Add", (dialog, which) -> {
                    Expense expense = new Expense(
                        new Timestamp(calendar.getTime()),
                            Integer.parseInt(String.valueOf(amt.getText())),
                            String.valueOf(desc.getText())
                    );
                    expenseRef.add(expense)
                            .addOnSuccessListener(documentReference ->
                                    Toast.makeText(ExpensesActivity.this, "Data has been added successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(ExpensesActivity.this, "Failed to add data" + e, Toast.LENGTH_SHORT).show());
                }).show();
    }
    int totalExpenses = 0;
    TextView totalExpensesView;
    public void refreshList(){
        totalExpensesView = findViewById(R.id.totalExpenseView);
        expenseRef.orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    LinearLayout expenseList = findViewById(R.id.expensesList);
                    expenseList.removeAllViews();
                    Handler handler = new Handler();
                    int delayMillis = 100, delay = 0;

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        handler.postDelayed(() -> {
                            Expense expense = documentSnapshot.toObject(Expense.class);
                            addExpenseItem(expense);
                            totalExpenses += expense.getAmount();
                            totalExpensesView.setText(pesoFormat.format(totalExpenses));
                        }, delay);
                        delay += delayMillis;
                    }
                    loadingExpense.setVisibility(ProgressBar.GONE);
                });
    }
    public void addExpenseItem(Expense expense){
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.expenses_card, null);

        TextView amt, desc, date;

        desc = view.findViewById(R.id.descExpenseTextView);
        date = view.findViewById(R.id.dateExpenseView);
        amt = view.findViewById(R.id.amtExpenseView);

        desc.setText(expense.getDescription());
        date.setText(expense.getConvertedDate());
        amt.setText(pesoFormat.format(expense.getAmount()));
        //amt.setText(String.format(getResources().getString(R.string.peso), expense.getAmount()));

        LinearLayout expenseList = findViewById(R.id.expensesList);
        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        view.startAnimation(animation);
        expenseList.addView(view);
    }
    public void backOnClick(View view){
        getOnBackPressedDispatcher().onBackPressed();
    }
}
class Expense{
    private int amount = 0;
    private String description = "";
    private String documentID;
    private Timestamp date;
    public Expense(){
        //no args for db
    }
    public Expense(Timestamp date, int amount, String description) {
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getDate() {
        return date;
    }
    @Exclude
    public String getConvertedDate(){
        return (String) DateFormat.format("EEE - MM/dd/yyyy", date.toDate());
    }
    @Exclude
    public String getDocumentID() {
        return documentID;
    }
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}