package com.example.individualassignment;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    TextView detailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailText = findViewById(R.id.textDetails);

        Bill bill = (Bill) getIntent().getSerializableExtra("bill");

        String details = "Month: " + bill.month +
                "\nUnits: " + bill.unitUsed +
                "\nRebate: " + bill.rebatePercent + "%" +
                "\nTotal: RM " + bill.totalCharge +
                "\nFinal Cost: RM " + bill.finalCost;

        detailText.setText(details);
    }
}

