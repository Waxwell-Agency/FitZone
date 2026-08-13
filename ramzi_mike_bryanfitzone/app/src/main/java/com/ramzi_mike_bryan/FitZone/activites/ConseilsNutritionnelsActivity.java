package com.ramzi_mike_bryan.FitZone.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.api.ApiService;
import com.api.RetrofitClient;
import com.models.Aliment;
import com.ramzi_mike_bryan.FitZone.R;
import com.ramzi_mike_bryan.FitZone.adapters.AlimentAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConseilsNutritionnelsActivity extends AppCompatActivity {

    private RecyclerView recyclerAliments;
    private TextView texteAucunAliment;
    private AlimentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conseils_nutritionnels);

        recyclerAliments = findViewById(R.id.recyclerAliments);
        texteAucunAliment = findViewById(R.id.texteAucunAliment);

        recyclerAliments.setLayoutManager(new LinearLayoutManager(this));

        TextView boutonRetour = findViewById(R.id.boutonRetourConseils);
        boutonRetour.setOnClickListener(v -> finish());

        chargerAliments();
    }

    private void chargerAliments() {

        ApiService apiService = RetrofitClient.getApiService();

        apiService.getAliments().enqueue(new Callback<List<Aliment>>() {

            @Override
            public void onResponse(Call<List<Aliment>> call, Response<List<Aliment>> response) {

                if (!response.isSuccessful() || response.body() == null) {

                    Toast.makeText(
                            ConseilsNutritionnelsActivity.this,
                            "Erreur lors du chargement des conseils nutritionnels",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                List<Aliment> aliments = response.body();

                adapter = new AlimentAdapter(
                        ConseilsNutritionnelsActivity.this,
                        aliments
                );

                recyclerAliments.setAdapter(adapter);

                texteAucunAliment.setVisibility(
                        aliments.isEmpty() ? View.VISIBLE : View.GONE
                );
            }

            @Override
            public void onFailure(Call<List<Aliment>> call, Throwable t) {

                Toast.makeText(
                        ConseilsNutritionnelsActivity.this,
                        "Impossible de contacter le serveur",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}
