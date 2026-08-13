package com.ramzi_mike_bryan.FitZone.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.api.ApiService;
import com.api.RetrofitClient;
import com.models.Programme;
import com.models.Quiz;
import com.models.Seance;
import com.models.User;
import com.ramzi_mike_bryan.FitZone.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccueilActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "FitZonePrefs";

    private TextView texteBonjour;
    private TextView texteResumeStatut;
    private TextView texteNbAFaire;
    private TextView texteNbEnRetard;
    private TextView texteNbValidees;
    private TextView texteNbProgrammes;
    private TextView texteNbQuiz;
    private LinearLayout containerSeances;
    private TextView texteAucuneSeance;
    private LinearLayout containerAnnonces;
    private TextView texteAucuneAnnonce;

    private String userId;
    private final List<Programme> programmesInscrits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        texteBonjour = findViewById(R.id.texteBonjour);
        texteResumeStatut = findViewById(R.id.texteResumeStatut);
        texteNbAFaire = findViewById(R.id.texteNbAFaire);
        texteNbEnRetard = findViewById(R.id.texteNbEnRetard);
        texteNbValidees = findViewById(R.id.texteNbValidees);
        texteNbProgrammes = findViewById(R.id.texteNbProgrammes);
        texteNbQuiz = findViewById(R.id.texteNbQuiz);
        containerSeances = findViewById(R.id.containerSeances);
        texteAucuneSeance = findViewById(R.id.texteAucuneSeance);
        containerAnnonces = findViewById(R.id.containerAnnonces);
        texteAucuneAnnonce = findViewById(R.id.texteAucuneAnnonce);

        Button boutonProgrammes = findViewById(R.id.boutonProgrammes);
        Button boutonQuiz = findViewById(R.id.boutonQuiz);

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

        sauvegarderUserId(userId);

        boutonProgrammes.setOnClickListener(v -> ouvrirActivite(ProgrammesActivity.class));
        boutonQuiz.setOnClickListener(v -> ouvrirActivite(QuizActivity.class));

        navAccueil.setOnClickListener(v -> {});

        navProgrammes.setOnClickListener(v -> ouvrirActivite(ProgrammesActivity.class));

        navSeances.setOnClickListener(v -> Toast.makeText(
                this,
                "Choisissez d'abord un programme",
                Toast.LENGTH_SHORT
        ).show());

        navQuiz.setOnClickListener(v -> ouvrirActivite(QuizActivity.class));

        navProfil.setOnClickListener(v -> ouvrirActivite(ProfilActivity.class));

        chargerUtilisateur();
    }

    private String recupererUserId() {

        String id = getIntent().getStringExtra("USER_ID");

        if (id != null) {
            return id;
        }

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getString("userId", null);
    }

    private void sauvegarderUserId(String id) {

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        preferences.edit().putString("userId", id).apply();
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

                    User user = response.body();
                    String prenom = user.getPrenom();

                    texteBonjour.setText(
                            "Bonjour, "
                                    + (prenom != null ? prenom : user.getUsername())
                                    + " !"
                    );

                    chargerProgrammes(user.getEnrolledProgramIds());

                } else {

                    Toast.makeText(
                            AccueilActivity.this,
                            "Impossible de charger l'utilisateur",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Toast.makeText(
                        AccueilActivity.this,
                        "Impossible de contacter le serveur",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void chargerProgrammes(List<String> enrolledProgramIds) {

        ApiService apiService = RetrofitClient.getApiService();

        apiService.getProgrammes().enqueue(new Callback<List<Programme>>() {

            @Override
            public void onResponse(Call<List<Programme>> call, Response<List<Programme>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    programmesInscrits.clear();

                    for (Programme programme : response.body()) {
                        if (enrolledProgramIds != null
                                && enrolledProgramIds.contains(programme.getId())) {
                            programmesInscrits.add(programme);
                        }
                    }

                    texteNbProgrammes.setText(
                            programmesInscrits.size() == 1
                                    ? "1 programme"
                                    : programmesInscrits.size() + " programmes"
                    );

                    afficherAnnonces();
                    chargerSeances(enrolledProgramIds);
                    chargerQuiz(enrolledProgramIds);

                } else {

                    Toast.makeText(
                            AccueilActivity.this,
                            "Erreur lors du chargement des programmes",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<Programme>> call, Throwable t) {

                Toast.makeText(
                        AccueilActivity.this,
                        "Impossible de contacter le serveur",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void chargerSeances(List<String> enrolledProgramIds) {

        ApiService apiService = RetrofitClient.getApiService();

        apiService.getSeances().enqueue(new Callback<List<Seance>>() {

            @Override
            public void onResponse(Call<List<Seance>> call, Response<List<Seance>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<Seance> seancesInscrites = new ArrayList<>();

                    for (Seance seance : response.body()) {
                        if (enrolledProgramIds != null
                                && enrolledProgramIds.contains(seance.getProgramId())) {
                            seancesInscrites.add(seance);
                        }
                    }

                    afficherResumeSeances(seancesInscrites);
                    afficherProchainesSeances(seancesInscrites);
                }
            }

            @Override
            public void onFailure(Call<List<Seance>> call, Throwable t) {
                // La bannière de résumé garde ses valeurs par défaut en cas d'échec.
            }
        });
    }

    private void chargerQuiz(List<String> enrolledProgramIds) {

        ApiService apiService = RetrofitClient.getApiService();

        apiService.getQuizzes().enqueue(new Callback<List<Quiz>>() {

            @Override
            public void onResponse(Call<List<Quiz>> call, Response<List<Quiz>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    int nbQuiz = 0;

                    for (Quiz quiz : response.body()) {
                        if (enrolledProgramIds != null
                                && enrolledProgramIds.contains(quiz.getProgramId())) {
                            nbQuiz++;
                        }
                    }

                    texteNbQuiz.setText(
                            nbQuiz == 1 ? "1 quiz" : nbQuiz + " quiz"
                    );
                }
            }

            @Override
            public void onFailure(Call<List<Quiz>> call, Throwable t) {
                // Le compteur de quiz garde sa valeur par défaut en cas d'échec.
            }
        });
    }

    private boolean estValidee(Seance seance) {
        String statut = seance.getStatus();
        return statut != null && statut.toLowerCase(Locale.ROOT).startsWith("valid");
    }

    private boolean estSoumise(Seance seance) {
        String statut = seance.getStatus();
        return statut != null && statut.toLowerCase(Locale.ROOT).startsWith("soumis");
    }

    private boolean estEnRetard(Seance seance) {

        if (estValidee(seance) || estSoumise(seance)) {
            return false;
        }

        String dueDate = seance.getDueDate();

        if (dueDate == null) {
            return false;
        }

        String aujourdhui = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.CANADA
        ).format(new Date());

        return dueDate.compareTo(aujourdhui) < 0;
    }

    private void afficherResumeSeances(List<Seance> seances) {

        int nbAFaire = 0;
        int nbEnRetard = 0;
        int nbValidees = 0;

        for (Seance seance : seances) {

            if (estValidee(seance)) {
                nbValidees++;
            } else if (estEnRetard(seance)) {
                nbEnRetard++;
            } else if (!estSoumise(seance)) {
                nbAFaire++;
            }
        }

        texteNbAFaire.setText(nbAFaire + "\nÀ faire");
        texteNbEnRetard.setText(nbEnRetard + "\nEn retard");
        texteNbValidees.setText(nbValidees + "\nValidées");

        texteResumeStatut.setText(
                "Vous avez " + seances.size() + " séance"
                        + (seances.size() > 1 ? "s" : "")
                        + " au total dans vos programmes."
        );
    }

    private void afficherProchainesSeances(List<Seance> seances) {

        containerSeances.removeAllViews();

        List<Seance> aVenir = new ArrayList<>();

        for (Seance seance : seances) {
            if (!estValidee(seance) && !estSoumise(seance)) {
                aVenir.add(seance);
            }
        }

        Collections.sort(aVenir, new Comparator<Seance>() {

            @Override
            public int compare(Seance s1, Seance s2) {
                String d1 = s1.getDueDate() == null ? "" : s1.getDueDate();
                String d2 = s2.getDueDate() == null ? "" : s2.getDueDate();
                return d1.compareTo(d2);
            }
        });

        int limite = Math.min(3, aVenir.size());

        for (int i = 0; i < limite; i++) {
            containerSeances.addView(creerCarteSeance(aVenir.get(i)));
        }

        texteAucuneSeance.setVisibility(aVenir.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private View creerCarteSeance(Seance seance) {

        LinearLayout carte = new LinearLayout(this);
        carte.setOrientation(LinearLayout.VERTICAL);
        carte.setBackgroundResource(R.drawable.bg_carte);
        carte.setPadding(dp(16), dp(14), dp(16), dp(14));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = dp(8);
        carte.setLayoutParams(params);

        TextView titre = new TextView(this);
        titre.setText(seance.getTitle());
        titre.setTextColor(Color.parseColor("#17152A"));
        titre.setTextSize(15);
        titre.setTypeface(titre.getTypeface(), Typeface.BOLD);
        carte.addView(titre);

        boolean enRetard = estEnRetard(seance);

        TextView details = new TextView(this);
        details.setText(
                "Échéance : "
                        + (seance.getDueDate() != null ? seance.getDueDate() : "N/A")
                        + (enRetard ? "  ·  En retard" : "")
        );
        details.setTextColor(Color.parseColor(enRetard ? "#D1453B" : "#77758A"));
        details.setTextSize(13);
        details.setPadding(0, dp(4), 0, 0);
        carte.addView(details);

        return carte;
    }

    private void afficherAnnonces() {

        containerAnnonces.removeAllViews();

        List<String> annonces = new ArrayList<>();

        for (Programme programme : programmesInscrits) {
            if (programme.getAnnonces() != null) {
                annonces.addAll(programme.getAnnonces());
            }
        }

        int limite = Math.min(3, annonces.size());

        for (int i = 0; i < limite; i++) {
            containerAnnonces.addView(creerCarteAnnonce(annonces.get(i)));
        }

        texteAucuneAnnonce.setVisibility(annonces.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private View creerCarteAnnonce(String texte) {

        LinearLayout carte = new LinearLayout(this);
        carte.setOrientation(LinearLayout.VERTICAL);
        carte.setBackgroundResource(R.drawable.bg_icone_violet_clair);
        carte.setPadding(dp(14), dp(12), dp(14), dp(12));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = dp(8);
        carte.setLayoutParams(params);

        TextView texteAnnonce = new TextView(this);
        texteAnnonce.setText(texte);
        texteAnnonce.setTextColor(Color.parseColor("#43266E"));
        texteAnnonce.setTextSize(13);
        carte.addView(texteAnnonce);

        return carte;
    }

    private int dp(int valeur) {
        float densite = getResources().getDisplayMetrics().density;
        return Math.round(valeur * densite);
    }
}
