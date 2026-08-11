package com.ramzi_mike_bryan.FitZone.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.api.ApiService;
import com.api.RetrofitClient;
import com.models.Programme;
import com.ramzi_mike_bryan.FitZone.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgrammesActivity extends AppCompatActivity {

    private List<Programme> programmes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programmes);

        Button boutonProgramme1 =
                findViewById(R.id.boutonVoirProgrammeMusculation);

        Button boutonProgramme2 =
                findViewById(R.id.boutonVoirProgrammePertePoids);

        TextView navAccueil =
                findViewById(R.id.navAccueil);

        // Charger les programmes du serveur
        chargerProgrammes();

        // Premier programme
        boutonProgramme1.setOnClickListener(v -> {

            if (programmes != null && !programmes.isEmpty()) {
                ouvrirProgramme(programmes.get(0));
            }
        });

        // Deuxième programme
        boutonProgramme2.setOnClickListener(v -> {

            if (programmes != null && programmes.size() > 1) {
                ouvrirProgramme(programmes.get(1));
            }
        });

        // Retour à l'accueil
        navAccueil.setOnClickListener(v -> finish());
    }

    private void chargerProgrammes() {

        ApiService apiService =
                RetrofitClient.getApiService();

        apiService.getProgrammes().enqueue(new Callback<List<Programme>>() {

            @Override
            public void onResponse(
                    Call<List<Programme>> call,
                    Response<List<Programme>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    programmes = response.body();

                    Toast.makeText(
                            ProgrammesActivity.this,
                            programmes.size() + " programmes reçus",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {

                    Toast.makeText(
                            ProgrammesActivity.this,
                            "Erreur lors du chargement",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<List<Programme>> call,
                    Throwable t) {

                Toast.makeText(
                        ProgrammesActivity.this,
                        "Impossible de contacter le serveur",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void ouvrirProgramme(Programme programme) {

        Intent intent = new Intent(
                ProgrammesActivity.this,
                DetailProgrammeActivity.class
        );

        intent.putExtra("programId", programme.getId());
        intent.putExtra("code", programme.getCode());
        intent.putExtra("title", programme.getTitle());
        intent.putExtra("description", programme.getDescription());
        intent.putExtra("coach", programme.getCoach());
        intent.putExtra("session", programme.getSession());

        startActivity(intent);
    }
}