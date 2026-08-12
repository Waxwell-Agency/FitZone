package com.ramzi_mike_bryan.FitZone.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.api.ApiService;
import com.api.RetrofitClient;
import com.models.Programme;
import com.models.User;
import com.ramzi_mike_bryan.FitZone.R;
import com.ramzi_mike_bryan.FitZone.adapters.ProgrammeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgrammesActivity extends AppCompatActivity {

    private RecyclerView recyclerProgrammes;
    private TextView texteNombreProgrammes;
    private EditText editRechercheProgramme;

    private ProgrammeAdapter adapter;

    private List<Programme> tousLesProgrammesUtilisateur =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_programmes);

        recyclerProgrammes =
                findViewById(R.id.recyclerProgrammes);

        texteNombreProgrammes =
                findViewById(R.id.texteNombreProgrammes);

        editRechercheProgramme =
                findViewById(R.id.editRechercheProgramme);

        TextView navAccueil =
                findViewById(R.id.navAccueil);

        TextView navProgrammes =
                findViewById(R.id.navProgrammes);


        TextView navSeances =
                findViewById(R.id.navSeances);

        TextView navQuiz =
                findViewById(R.id.navQuiz);

        TextView navProfil =
                findViewById(R.id.navProfil);

        recyclerProgrammes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        Button boutonTous =
                findViewById(R.id.boutonTous);

        Button boutonActifs =
                findViewById(R.id.boutonActifs);

        Button boutonTermines =
                findViewById(R.id.boutonTermines);

        // NAVIGATION

        navAccueil.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProgrammesActivity.this,
                    AccueilActivity.class
            );

            startActivity(intent);
            finish();
        });
        navProgrammes.setOnClickListener(v -> {

        });

        navQuiz.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProgrammesActivity.this,
                    QuizActivity.class
            );

            startActivity(intent);
        });

        navProfil.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProgrammesActivity.this,
                    ProfilActivity.class
            );

            startActivity(intent);
        });

        navSeances.setOnClickListener(v -> {

            Toast.makeText(
                    ProgrammesActivity.this,
                    "Choisissez d'abord un programme",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // RECHERCHE

        editRechercheProgramme.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        rechercherProgramme(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                }
        );

        chargerUtilisateurConnecte();
        boutonTous.setOnClickListener(v -> {

            if (adapter != null) {
                adapter.mettreAJourListe(
                        new ArrayList<>(
                                tousLesProgrammesUtilisateur
                        )
                );

                mettreAJourNombre(
                        tousLesProgrammesUtilisateur.size()
                );
            }
        });

        boutonActifs.setOnClickListener(v -> {

            if (adapter != null) {
                adapter.mettreAJourListe(
                        new ArrayList<>(
                                tousLesProgrammesUtilisateur
                        )
                );

                mettreAJourNombre(
                        tousLesProgrammesUtilisateur.size()
                );
            }
        });

        boutonTermines.setOnClickListener(v -> {

            if (adapter != null) {
                adapter.mettreAJourListe(
                        new ArrayList<>()
                );

                mettreAJourNombre(0);
            }
        });
    }

    private void chargerUtilisateurConnecte() {

        SharedPreferences preferences =
                getSharedPreferences(
                        "FitZonePrefs",
                        MODE_PRIVATE
                );

        String userId =
                preferences.getString(
                        "userId",
                        null
                );

        if (userId == null) {

            Toast.makeText(
                    this,
                    "Utilisateur non connecté",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        ApiService apiService =
                RetrofitClient.getApiService();

        apiService.getUserById(userId).enqueue(
                new Callback<User>() {

                    @Override
                    public void onResponse(
                            Call<User> call,
                            Response<User> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            User user = response.body();

                            chargerProgrammes(
                                    user.getEnrolledProgramIds()
                            );

                        } else {

                            Toast.makeText(
                                    ProgrammesActivity.this,
                                    "Impossible de charger l'utilisateur",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<User> call,
                            Throwable t) {

                        Toast.makeText(
                                ProgrammesActivity.this,
                                "Erreur de connexion",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void chargerProgrammes(
            List<String> enrolledProgramIds) {

        ApiService apiService =
                RetrofitClient.getApiService();

        apiService.getProgrammes().enqueue(
                new Callback<List<Programme>>() {

                    @Override
                    public void onResponse(
                            Call<List<Programme>> call,
                            Response<List<Programme>> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            tousLesProgrammesUtilisateur.clear();

                            for (Programme programme :
                                    response.body()) {

                                if (enrolledProgramIds != null
                                        && enrolledProgramIds.contains(
                                        programme.getId())) {

                                    tousLesProgrammesUtilisateur.add(
                                            programme
                                    );
                                }
                            }

                            adapter =
                                    new ProgrammeAdapter(
                                            ProgrammesActivity.this,
                                            new ArrayList<>(
                                                    tousLesProgrammesUtilisateur
                                            )
                                    );

                            recyclerProgrammes.setAdapter(
                                    adapter
                            );

                            mettreAJourNombre(
                                    tousLesProgrammesUtilisateur.size()
                            );

                        } else {

                            Toast.makeText(
                                    ProgrammesActivity.this,
                                    "Erreur lors du chargement des programmes",
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

    private void rechercherProgramme(
            String recherche) {

        if (adapter == null) {
            return;
        }

        List<Programme> resultat =
                new ArrayList<>();

        String texte =
                recherche
                        .trim()
                        .toLowerCase();

        for (Programme programme :
                tousLesProgrammesUtilisateur) {

            String titre =
                    programme.getTitle() == null
                            ? ""
                            : programme.getTitle()
                            .toLowerCase();

            String code =
                    programme.getCode() == null
                            ? ""
                            : programme.getCode()
                            .toLowerCase();

            String coach =
                    programme.getCoach() == null
                            ? ""
                            : programme.getCoach()
                            .toLowerCase();

            if (titre.contains(texte)
                    || code.contains(texte)
                    || coach.contains(texte)) {

                resultat.add(programme);
            }
        }

        adapter.mettreAJourListe(
                resultat
        );

        mettreAJourNombre(
                resultat.size()
        );
    }

    private void mettreAJourNombre(
            int nombre) {

        if (nombre == 1) {

            texteNombreProgrammes.setText(
                    "1 programme"
            );

        } else {

            texteNombreProgrammes.setText(
                    nombre + " programmes"
            );
        }
    }


}