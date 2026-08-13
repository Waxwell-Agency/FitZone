package com.ramzi_mike_bryan.FitZone.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.api.ApiService;
import com.api.RetrofitClient;
import com.models.Programme;
import com.models.Quiz;
import com.models.User;
import com.ramzi_mike_bryan.FitZone.R;
import com.ramzi_mike_bryan.FitZone.adapters.QuizAdapter;
import com.ramzi_mike_bryan.FitZone.dao.QuizResult;
import com.ramzi_mike_bryan.FitZone.dao.QuizResultDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "FitZonePrefs";

    private RecyclerView recyclerQuiz;
    private TextView texteNbQuizDisponibles;
    private TextView texteAucunQuiz;

    private QuizAdapter adapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        recyclerQuiz = findViewById(R.id.recyclerQuiz);
        texteNbQuizDisponibles = findViewById(R.id.texteNbQuizDisponibles);
        texteAucunQuiz = findViewById(R.id.texteAucunQuiz);

        recyclerQuiz.setLayoutManager(new LinearLayoutManager(this));

        TextView navAccueil = findViewById(R.id.navAccueil);
        TextView navProgrammes = findViewById(R.id.navProgrammes);
        TextView navSeances = findViewById(R.id.navSeances);
        TextView navQuiz = findViewById(R.id.navQuiz);
        TextView navProfil = findViewById(R.id.navProfil);

        userId = recupererUserId();

        if (userId == null) {

            Toast.makeText(
                    this,
                    "Utilisateur non connecté",
                    Toast.LENGTH_SHORT
            ).show();

            startActivity(new Intent(this, ConnexionActivity.class));
            finish();
            return;
        }

        navAccueil.setOnClickListener(v -> ouvrirActivite(AccueilActivity.class));
        navProgrammes.setOnClickListener(v -> ouvrirActivite(ProgrammesActivity.class));

        navSeances.setOnClickListener(v -> Toast.makeText(
                this,
                "Choisissez d'abord un programme",
                Toast.LENGTH_SHORT
        ).show());

        navQuiz.setOnClickListener(v -> {});
        navProfil.setOnClickListener(v -> ouvrirActivite(ProfilActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userId != null) {
            chargerUtilisateur();
        }
    }

    private String recupererUserId() {

        String id = getIntent().getStringExtra("USER_ID");

        if (id != null) {
            return id;
        }

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getString("userId", null);
    }

    private void ouvrirActivite(Class<?> activite) {

        Intent intent = new Intent(this, activite);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void chargerUtilisateur() {

        ApiService apiService = RetrofitClient.getApiService();

        apiService.getUserById(userId).enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful() && response.body() != null) {
                    chargerProgrammesEtQuiz(response.body().getEnrolledProgramIds());
                } else {
                    Toast.makeText(
                            QuizActivity.this,
                            "Impossible de charger l'utilisateur",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Toast.makeText(
                        QuizActivity.this,
                        "Impossible de contacter le serveur",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void chargerProgrammesEtQuiz(List<String> enrolledProgramIds) {

        ApiService apiService = RetrofitClient.getApiService();

        apiService.getProgrammes().enqueue(new Callback<List<Programme>>() {

            @Override
            public void onResponse(Call<List<Programme>> call, Response<List<Programme>> response) {

                Map<String, String> titresProgrammes = new HashMap<>();

                if (response.isSuccessful() && response.body() != null) {
                    for (Programme programme : response.body()) {
                        titresProgrammes.put(programme.getId(), programme.getTitle());
                    }
                }

                chargerQuiz(enrolledProgramIds, titresProgrammes);
            }

            @Override
            public void onFailure(Call<List<Programme>> call, Throwable t) {
                chargerQuiz(enrolledProgramIds, new HashMap<>());
            }
        });
    }

    private void chargerQuiz(
            List<String> enrolledProgramIds,
            Map<String, String> titresProgrammes) {

        ApiService apiService = RetrofitClient.getApiService();

        apiService.getQuizzes().enqueue(new Callback<List<Quiz>>() {

            @Override
            public void onResponse(Call<List<Quiz>> call, Response<List<Quiz>> response) {

                if (!response.isSuccessful() || response.body() == null) {

                    Toast.makeText(
                            QuizActivity.this,
                            "Erreur lors du chargement des quiz",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                List<Quiz> quizDisponibles = new ArrayList<>();

                for (Quiz quiz : response.body()) {
                    if (enrolledProgramIds != null
                            && enrolledProgramIds.contains(quiz.getProgramId())) {
                        quizDisponibles.add(quiz);
                    }
                }

                QuizResultDao dao = new QuizResultDao(QuizActivity.this);
                Map<String, QuizResult> resultats = dao.getResultatsUtilisateur(userId);

                adapter = new QuizAdapter(
                        QuizActivity.this,
                        quizDisponibles,
                        titresProgrammes,
                        resultats
                );

                recyclerQuiz.setAdapter(adapter);

                texteNbQuizDisponibles.setText(
                        quizDisponibles.size() == 1
                                ? "1 quiz"
                                : quizDisponibles.size() + " quiz"
                );

                texteAucunQuiz.setVisibility(
                        quizDisponibles.isEmpty() ? View.VISIBLE : View.GONE
                );
            }

            @Override
            public void onFailure(Call<List<Quiz>> call, Throwable t) {

                Toast.makeText(
                        QuizActivity.this,
                        "Impossible de contacter le serveur",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}
