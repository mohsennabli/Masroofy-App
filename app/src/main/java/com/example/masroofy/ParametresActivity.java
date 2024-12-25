package com.example.masroofy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ParametresActivity extends AppCompatActivity {

    private EditText editTextBudget, editTextCategory;
    private TextView textViewBudgetPrompt;
    private Button btnSaveBudget, btnClearExpenses, btnAddCategory, btnDeleteCategory, btnGoBack;
    private Spinner spinnerCategories;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> categoryAdapter;
    private ArrayList<String> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        dbHelper = new DatabaseHelper(this);

        editTextBudget = findViewById(R.id.editTextBudget);
        textViewBudgetPrompt = findViewById(R.id.textViewBudgetPrompt);
        editTextCategory = findViewById(R.id.editTextCategory);
        btnSaveBudget = findViewById(R.id.btnSaveBudget);
        btnClearExpenses = findViewById(R.id.btnClearExpenses);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnDeleteCategory = findViewById(R.id.btnDeleteCategory);
        btnGoBack = findViewById(R.id.btnGoBack);
        spinnerCategories = findViewById(R.id.spinnerCategories);

        loadCurrentBudget();

        loadCategories();

        // Enregistrer le budget mensuel
        btnSaveBudget.setOnClickListener(v -> {
            String budget = editTextBudget.getText().toString().trim();
            if (!budget.isEmpty()) {
                getSharedPreferences("Settings", MODE_PRIVATE)
                        .edit().putString("monthly_budget", budget).apply();
                Toast.makeText(this, "Budget enregistré!", Toast.LENGTH_SHORT).show();

                // Mettre à jour le TextView avec le budget enregistré
                textViewBudgetPrompt.setText("Budget actuel : " + budget + " Dinars");
                editTextBudget.setText(""); // Effacer le champ
            } else {
                Toast.makeText(this, "Veuillez entrer un budget valide.", Toast.LENGTH_SHORT).show();
            }
        });

        // Effacer toutes les dépenses
        btnClearExpenses.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(DatabaseHelper.TABLE_EXPENSES, null, null);
            db.close();
            Toast.makeText(this, "Toutes les dépenses ont été supprimées!", Toast.LENGTH_SHORT).show();
        });

        // Ajouter une catégorie
        btnAddCategory.setOnClickListener(v -> {
            String category = editTextCategory.getText().toString().trim();
            if (!category.isEmpty()) {
                long result = dbHelper.insertCategory(category);
                if (result != -1) {
                    Toast.makeText(this, "Catégorie ajoutée!", Toast.LENGTH_SHORT).show();
                    editTextCategory.setText("");
                    loadCategories();
                } else {
                    Toast.makeText(this, "La catégorie existe déjà!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Supprimer une catégorie sélectionnée
        btnDeleteCategory.setOnClickListener(v -> {
            String selectedCategory = spinnerCategories.getSelectedItem().toString();
            if (!selectedCategory.isEmpty()) {
                int rowsDeleted = dbHelper.deleteCategory(selectedCategory);
                if (rowsDeleted > 0) {
                    Toast.makeText(this, "Catégorie supprimée!", Toast.LENGTH_SHORT).show();
                    loadCategories();
                }
            }
        });

        // Retourner à MainActivity
        btnGoBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void loadCurrentBudget() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String budget = prefs.getString("monthly_budget", "Non défini");
        textViewBudgetPrompt.setText("Budget actuel : " + budget + " Dinars");
    }

    private void loadCategories() {
        categoryList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllCategories();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                categoryList.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME)));
            }
            cursor.close();
        }

        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(categoryAdapter);
    }
}
