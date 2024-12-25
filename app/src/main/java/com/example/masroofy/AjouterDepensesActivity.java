package com.example.masroofy;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AjouterDepensesActivity extends AppCompatActivity {

    private EditText editMontant, editDescription;
    private Spinner spinnerCategory;
    private Button btnEnregistrer, btnGoBack;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_depenses);
        editMontant = findViewById(R.id.edit_montant);
        editDescription = findViewById(R.id.edit_description);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnEnregistrer = findViewById(R.id.btn_enregistrer);
        btnGoBack = findViewById(R.id.btnGoBack);

        dbHelper = new DatabaseHelper(this);

        loadCategories();

        btnEnregistrer.setOnClickListener(v -> enregistrerDepense());

        // Go back to MainActivity
        btnGoBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void loadCategories() {
        ArrayList<String> categories = new ArrayList<>();
        Cursor cursor = dbHelper.getAllCategories();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                categories.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME)));
            }
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void enregistrerDepense() {
        String montantStr = editMontant.getText().toString().trim();
        String categorie = spinnerCategory.getSelectedItem().toString().trim();
        String description = editDescription.getText().toString().trim();

        if (montantStr.isEmpty() || categorie.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        double montant = Double.parseDouble(montantStr);

        // Fetch saved monthly budget
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String budgetStr = prefs.getString("monthly_budget", "0");
        double monthlyBudget = Double.parseDouble(budgetStr);

        // Calculate current month's total expenses
        double totalCurrentExpenses = getTotalCurrentMonthExpenses();

        // Check if adding the new expense exceeds the budget
        if ((totalCurrentExpenses + montant) > monthlyBudget) {
            Toast.makeText(this, "Dépense non ajoutée : Vous avez dépassé votre budget mensuel!", Toast.LENGTH_LONG).show();
            return;
        }

        // Insert into the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_AMOUNT, montant);
        values.put(DatabaseHelper.COLUMN_CATEGORY, categorie);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_DATE, System.currentTimeMillis());

        long newRowId = db.insert(DatabaseHelper.TABLE_EXPENSES, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Dépense enregistrée avec succès", Toast.LENGTH_SHORT).show();

            // Aller à MainActivity après ajout réussi
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
        }
    }

    private double getTotalCurrentMonthExpenses() {
        double total = 0;
        Cursor cursor = dbHelper.getCurrentMonthExpenses();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            }
            cursor.close();
        }
        return total;
    }
}
