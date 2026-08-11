package com.ramzi_mike_bryan.FitZone.activites;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.api.ApiService;
import com.api.RetrofitClient;
import com.models.Programme;
import com.ramzi_mike_bryan.FitZone.R;
import com.ramzi_mike_bryan.FitZone.adapters.ProgrammeAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgrammesActivity extends AppCompatActivity {

    private RecyclerView recyclerProgrammes;
    private TextView texteNombreProgrammes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programmes);

        recyclerProgrammes = findViewById(R.id.recyclerProgrammes);
        texteNombreProgrammes = findViewById(R.id.texteNombreProgrammes);

        TextView navAccueil = findViewById(R.id.navAccueil);

        recyclerProgrammes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        navAccueil.setOnClickListener(v -> finish());

        chargerProgrammes();
    }

    private void chargerProgrammes() {

        ApiService apiService = RetrofitClient.getApiService();

        apiService.getProgrammes().enqueue(
                new Callback<List<Programme>>() {

                    @Override
                    public void onResponse(
                            Call<List<Programme>> call,
                            Response<List<Programme>> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            List<Programme> programmes =
                                    response.body();

                            ProgrammeAdapter adapter =
                                    new ProgrammeAdapter(
                                            ProgrammesActivity.this,
                                            programmes
                                    );

                            recyclerProgrammes.setAdapter(adapter);

                            texteNombreProgrammes.setText(
                                    programmes.size() + " programmes"
                            );

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
                }
        );
    }
}