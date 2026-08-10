package com.ramzi_mike_bryan.FitZone.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ramzi_mike_bryan.FitZone.R;

public class InscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        TextView boutonRetourConnexion = findViewById(R.id.boutonRetourConnexion);

        boutonRetourConnexion.setOnClickListener(v -> {
            Intent intent = new Intent(InscriptionActivity.this, ConnexionActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
