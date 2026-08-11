package com.ramzi_mike_bryan.FitZone.activites;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ramzi_mike_bryan.FitZone.R;

public class SeancesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seances);

        TextView boutonRetour =
                findViewById(R.id.boutonRetourSeances);

        boutonRetour.setOnClickListener(v -> finish());
    }
}