package com.ramzi_mike_bryan.FitZone.activites;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.api.ApiService;
import com.api.RetrofitClient;
import com.models.Seance;
import com.ramzi_mike_bryan.FitZone.R;
import com.ramzi_mike_bryan.FitZone.adapters.SeanceAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeancesActivity extends AppCompatActivity {

    private RecyclerView recyclerSeances;
    private TextView texteNombreSeances;

    private String programId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seances);

        recyclerSeances =
                findViewById(R.id.recyclerSeances);

        texteNombreSeances =
                findViewById(R.id.texteNombreSeances);

        TextView texteProgramme =
                findViewById(R.id.texteProgrammeSeances);

        TextView boutonRetour =
                findViewById(R.id.boutonRetourSeances);

        programId = getIntent().getStringExtra("programId");

        String programTitle =
                getIntent().getStringExtra("programTitle");

        texteProgramme.setText(
                "Programme : " + programTitle
        );

        recyclerSeances.setLayoutManager(
                new LinearLayoutManager(this)
        );

        boutonRetour.setOnClickListener(v -> finish());

        chargerSeances();
    }

    private void chargerSeances() {

        ApiService apiService =
                RetrofitClient.getApiService();

        apiService.getSeances().enqueue(
                new Callback<List<Seance>>() {

                    @Override
                    public void onResponse(
                            Call<List<Seance>> call,
                            Response<List<Seance>> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            List<Seance> toutesLesSeances =
                                    response.body();

                            List<Seance> seancesProgramme =
                                    new ArrayList<>();

                            for (Seance seance : toutesLesSeances) {

                                if (programId != null
                                        && programId.equals(
                                        seance.getProgramId())) {

                                    seancesProgramme.add(seance);
                                }
                            }

                            SeanceAdapter adapter =
                                    new SeanceAdapter(
                                            seancesProgramme
                                    );

                            recyclerSeances.setAdapter(adapter);

                            texteNombreSeances.setText(
                                    seancesProgramme.size()
                                            + " séances"
                            );

                        } else {

                            Toast.makeText(
                                    SeancesActivity.this,
                                    "Erreur lors du chargement",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<Seance>> call,
                            Throwable t) {

                        Toast.makeText(
                                SeancesActivity.this,
                                "Impossible de contacter le serveur",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }
}