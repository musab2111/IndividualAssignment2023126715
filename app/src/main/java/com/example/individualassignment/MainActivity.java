package com.example.individualassignment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    EditText editTextUnit, editTextRebate;
    TextView textTotal, textFinal;
    Button btnCalculate, btnViewHistory, btnAbout;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        editTextUnit = findViewById(R.id.editTextUnit);
        editTextRebate = findViewById(R.id.editTextRebate);
        textTotal = findViewById(R.id.textTotal);
        textFinal = findViewById(R.id.textFinal);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnViewHistory = findViewById(R.id.btnViewHistory);
        btnAbout = findViewById(R.id.btnAbout);

        dbRef = FirebaseDatabase.getInstance().getReference("Bills");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList("January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        btnCalculate.setOnClickListener(v -> calculateAndSave());

        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private void calculateAndSave() {
        String unitText = editTextUnit.getText().toString();
        String rebateText = editTextRebate.getText().toString();


        if (unitText.isEmpty()) {
            editTextUnit.setError("Electricity used cannot be empty");
            Toast.makeText(this, "Please enter electricity units", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rebateText.isEmpty()) {
            editTextRebate.setError("Rebate cannot be empty");
            Toast.makeText(this, "Please enter rebate percentage", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String month = spinnerMonth.getSelectedItem().toString();
            double unitUsed = Double.parseDouble(unitText);
            double rebateInput = Double.parseDouble(rebateText);


            if (rebateInput < 0 || rebateInput > 5) {
                editTextRebate.setError("Rebate must be between 0% and 5%");
                Toast.makeText(this, "Rebate must be between 0% and 5%", Toast.LENGTH_LONG).show();
                return;
            }


            double rebate = rebateInput / 100;

            double totalCharge = calculateBill(unitUsed);
            double finalCost = totalCharge - (totalCharge * rebate);

            textTotal.setText("Total Charges: RM " + String.format("%.2f", totalCharge));
            textFinal.setText("Final Cost: RM " + String.format("%.2f", finalCost));

            String id = dbRef.push().getKey();
            Bill bill = new Bill(id, month, unitUsed, rebateInput, totalCharge, finalCost); // Store raw rebateInput
            dbRef.child(id).setValue(bill)
                    .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this, "Bill saved successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed to save bill: " + e.getMessage(), Toast.LENGTH_LONG).show());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for units and rebate.", Toast.LENGTH_LONG).show();

            editTextUnit.setError(null);
            editTextRebate.setError(null);
        }
    }

    private double calculateBill(double unit) {
        double total = 0;
        if (unit <= 200) {
            total = unit * 0.218;
        } else if (unit <= 300) {
            total = 200 * 0.218 + (unit - 200) * 0.334;
        } else if (unit <= 600) {
            total = 200 * 0.218 + 100 * 0.334 + (unit - 300) * 0.516;
        } else {
            total = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (unit - 600) * 0.546;
        }
        return total;
    }
}