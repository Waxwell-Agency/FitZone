package com.ramzi_mike_bryan.FitZone.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ramzi_mike_bryan.FitZone.R;

public class DetailProgrammeActivity extends AppCompatActivity {

    private String programId;
    private String programTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_programme);

        // Éléments de la page
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

        Button boutonVoirQuiz =
                findViewById(R.id.boutonVoirQuiz);


        // =====================================
        // RÉCUPÉRER LE PROGRAMME SÉLECTIONNÉ
        // =====================================

        Intent intent = getIntent();

        programId =
                intent.getStringExtra("programId");

        programTitle =
                intent.getStringExtra("title");

        String code =
                intent.getStringExtra("code");

        String coach =
                intent.getStringExtra("coach");

        String session =
                intent.getStringExtra("session");

        String description =
                intent.getStringExtra("description");


        // =====================================
        // AFFICHER LES INFORMATIONS
        // =====================================

        texteNom.setText(programTitle);

        texteCode.setText(
                code + " • " + session
        );

        texteCoach.setText(coach);

        texteDescription.setText(description);


        // =====================================
        // RETOUR VERS PROGRAMMES
        // =====================================

        boutonRetour.setOnClickListener(v -> {
            finish();
        });


        // =====================================
        // ALLER VERS LES SÉANCES
        // =====================================

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
                    programTitle
            );

            startActivity(intentSeances);
        });


        // =====================================
        // ALLER VERS LES QUIZ
        // =====================================

        boutonVoirQuiz.setOnClickListener(v -> {

            Intent intentQuiz = new Intent(
                    DetailProgrammeActivity.this,
                    QuizActivity.class
            );

            intentQuiz.putExtra(
                    "programId",
                    programId
            );

            startActivity(intentQuiz);
        });
    }
}