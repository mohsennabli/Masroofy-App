package com.example.masroofy;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ResumeActivity extends AppCompatActivity {

    private PieChart pieChart;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        pieChart = findViewById(R.id.pieChart);
        dbHelper = new DatabaseHelper(this);

        // Load data into the PieChart
        loadPieChartData();
        Button btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

    }

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // Fetch expenses grouped by category from the database
        Cursor cursor = dbHelper.getExpensesByCategory();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY));
                float total = cursor.getFloat(cursor.getColumnIndexOrThrow("total"));
                entries.add(new PieEntry(total, category));
                Log.d("PieChartData", "Category: " + category + ", Total: " + total);
            }
            cursor.close();
        }

        if (entries.isEmpty()) {
            Log.d("PieChartData", "No data available to display in PieChart");
        }

        // Create the PieDataSet and PieData
        PieDataSet dataSet = new PieDataSet(entries, "Dépenses par Catégorie");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(android.graphics.Color.BLACK);

        PieData pieData = new PieData(dataSet);

        // Configure the PieChart
        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Total Dépenses");
        pieChart.setCenterTextSize(18f);
    }
}
