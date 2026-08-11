package com.ramzi_mike_bryan.FitZone.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ramzi_mike_bryan.FitZone.R;

public class ProgrammesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programmes);

        Button boutonVoirProgrammeMusculation =
                findViewById(R.id.boutonVoirProgrammeMusculation);

        boutonVoirProgrammeMusculation.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ProgrammesActivity.this,
                    DetailProgrammeActivity.class
            );

            startActivity(intent);
        });
    }
}