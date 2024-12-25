package com.example.masroofy;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoriqueDepensesActivity extends AppCompatActivity {

    private ListView listViewExpenses;
    private DatabaseHelper dbHelper;
    private ArrayList<String> expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_depenses);

        listViewExpenses = findViewById(R.id.listViewExpenses);
        dbHelper = new DatabaseHelper(this);
        expenseList = new ArrayList<>();

        loadAllExpenses();
        Button btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

    }

    private void loadAllExpenses() {
        Cursor cursor = dbHelper.getAllExpenses();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY));
                String amount = String.valueOf(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT)));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                expenseList.add("Catégorie: " + category + " | Montant: " + amount + " | Date: " + date);
            }
            cursor.close();
        }

        if (expenseList.isEmpty()) {
            Toast.makeText(this, "Aucune dépense enregistrée.", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        listViewExpenses.setAdapter(adapter);
    }
}
