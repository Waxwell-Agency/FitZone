package com.ramzi_mike_bryan.FitZone.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ramzi_mike_bryan.FitZone.R;

public class DetailProgrammeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_programme);

        TextView boutonRetour =
                findViewById(R.id.boutonRetourProgramme);

        Button boutonVoirSeances =
                findViewById(R.id.boutonVoirSeances);

        boutonRetour.setOnClickListener(v -> finish());

        boutonVoirSeances.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DetailProgrammeActivity.this,
                    SeancesActivity.class
            );

            startActivity(intent);
        });
    }
}