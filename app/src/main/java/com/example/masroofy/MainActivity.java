package com.example.masroofy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAjouter = findViewById(R.id.btn_ajouter_depenses);
        Button btnHistorique = findViewById(R.id.btn_historique_depenses);
        Button btnResume = findViewById(R.id.btn_resume);

        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AjouterDepensesActivity.class);
                startActivity(intent);
            }
        });

        btnHistorique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoriqueDepensesActivity.class);
                startActivity(intent);
            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResumeActivity.class);
                startActivity(intent);
            }
        });
        Button btnParametres = findViewById(R.id.btnParametres);
        btnParametres.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ParametresActivity.class);
            startActivity(intent);
        });

    }


}
