package com.ramzi_mike_bryan.FitZone.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ramzi_mike_bryan.FitZone.R;

public class DetailProgrammeActivity extends AppCompatActivity {

    private String programId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_programme);

        TextView boutonRetour =
                findViewById(R.id.boutonRetourProgramme);

        TextView texteNom =
                findViewById(R.id.texteNomProgramme);

        TextView texteCode =
                findViewById(R.id.texteCodeProgramme);

        TextView texteCoach =
                findViewById(R.id.texteCoachProgramme);

        TextView texteDescription =
                findViewById(R.id.texteDescriptionProgramme);

        Button boutonVoirSeances =
                findViewById(R.id.boutonVoirSeances);

        // Récupération des informations envoyées par ProgrammeAdapter
        Intent intent = getIntent();

        programId = intent.getStringExtra("programId");

        String title = intent.getStringExtra("title");
        String code = intent.getStringExtra("code");
        String coach = intent.getStringExtra("coach");
        String session = intent.getStringExtra("session");
        String description = intent.getStringExtra("description");

        // Affichage des vraies données
        texteNom.setText(title);

        texteCode.setText(
                code + " • " + session
        );

        texteCoach.setText(coach);

        texteDescription.setText(description);

        // Retour
        boutonRetour.setOnClickListener(v -> finish());

        // Aller vers les séances du programme
        boutonVoirSeances.setOnClickListener(v -> {

            Intent intentSeances = new Intent(
                    DetailProgrammeActivity.this,
                    SeancesActivity.class
            );

            intentSeances.putExtra(
                    "programId",
                    programId
            );

            intentSeances.putExtra(
                    "programTitle",
                    title
            );

            startActivity(intentSeances);
        });
    }
}