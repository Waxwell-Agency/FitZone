package com.ramzi_mike_bryan.FitZone.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ramzi_mike_bryan.FitZone.R;

public class AccueilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        Button boutonProgrammes = findViewById(R.id.bouton_programmes);

        boutonProgrammes.setOnClickListener(v -> {
            Intent intent = new Intent(
                    AccueilActivity.this,
                    ProgrammesActivity.class
            );

            startActivity(intent);
        });
    }
}