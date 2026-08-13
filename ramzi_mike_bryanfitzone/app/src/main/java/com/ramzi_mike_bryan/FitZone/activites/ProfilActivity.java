package com.ramzi_mike_bryan.FitZone.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.api.ApiService;
import com.api.RetrofitClient;
import com.models.User;
import com.ramzi_mike_bryan.FitZone.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "FitZonePrefs";

    private EditText champPrenom;
    private EditText champNom;
    private EditText champEmail;
    private EditText champTelephone;
    private EditText champPhotoUrl;
    private EditText champMotDePasse;

    private Button boutonModifierProfil;
    private Button boutonDeconnexion;

    private TextView navAccueil;
    private TextView navProgrammes;
    private TextView navSeances;
    private TextView navQuiz;
    private TextView navProfil;

    private ApiService apiService;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Champs du profil
        champPrenom = findViewById(R.id.champPrenom);
        champNom = findViewById(R.id.champNom);
        champEmail = findViewById(R.id.champEmail);
        champTelephone = findViewById(R.id.champTelephone);
        champPhotoUrl = findViewById(R.id.champPhotoUrl);
        champMotDePasse = findViewById(R.id.champMotDePasse);

        boutonModifierProfil = findViewById(R.id.boutonModifierProfil);
        boutonDeconnexion = findViewById(R.id.boutonDeconnexion);

        // Navigation
        navAccueil = findViewById(R.id.navAccueil);
        navProgrammes = findViewById(R.id.navProgrammes);
        navSeances = findViewById(R.id.navSeances);
        navQuiz = findViewById(R.id.navQuiz);
        navProfil = findViewById(R.id.navProfil);

        apiService = RetrofitClient.getApiService();

        userId = recupererUserId();

        if (userId == null) {
            Toast.makeText(
                    this,
                    "Utilisateur introuvable",
                    Toast.LENGTH_SHORT
            ).show();

            startActivity(new Intent(this, ConnexionActivity.class));
            finish();
            return;
        }

        chargerProfil();

        // Modifier le profil
        boutonModifierProfil.setOnClickListener(v -> modifierProfil());

        // Déconnexion
        boutonDeconnexion.setOnClickListener(v -> deconnecter());

        // Navigation inférieure
        navAccueil.setOnClickListener(v -> ouvrirActivite(AccueilActivity.class));

        navProgrammes.setOnClickListener(
                v -> ouvrirActivite(ProgrammesActivity.class)
        );

        navSeances.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Choisissez d'abord un programme",
                        Toast.LENGTH_SHORT
                ).show()
        );

        navQuiz.setOnClickListener(
                v -> ouvrirActivite(QuizActivity.class)
        );

        navProfil.setOnClickListener(v -> {
            // On est déjà sur le profil
        });
    }

    private String recupererUserId() {

        String id = getIntent().getStringExtra("USER_ID");

        if (id != null) {
            return id;
        }

        SharedPreferences preferences =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        return preferences.getString("userId", null);
    }

    private void sauvegarderUserId(String id) {

        SharedPreferences preferences =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        preferences.edit()
                .putString("userId", id)
                .apply();
    }

    private void ouvrirActivite(Class<?> activite) {

        Intent intent = new Intent(this, activite);
        intent.putExtra("USER_ID", userId);

        startActivity(intent);
    }

    private void chargerProfil() {

        apiService.getUserById(userId).enqueue(new Callback<User>() {

            @Override
            public void onResponse(
                    Call<User> call,
                    Response<User> response) {

                if (response.isSuccessful() && response.body() != null) {

                    User user = response.body();

                    sauvegarderUserId(user.getId());

                    champPrenom.setText(
                            user.getPrenom() != null
                                    ? user.getPrenom()
                                    : ""
                    );

                    champNom.setText(
                            user.getNom() != null
                                    ? user.getNom()
                                    : ""
                    );

                    champEmail.setText(
                            user.getEmail() != null
                                    ? user.getEmail()
                                    : ""
                    );

                    champTelephone.setText(
                            user.getTelephone() != null
                                    ? user.getTelephone()
                                    : ""
                    );

                    champPhotoUrl.setText(
                            user.getPhotoUrl() != null
                                    ? user.getPhotoUrl()
                                    : ""
                    );

                } else {

                    Toast.makeText(
                            ProfilActivity.this,
                            "Impossible de charger le profil",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<User> call,
                    Throwable t) {

                Toast.makeText(
                        ProfilActivity.this,
                        "Erreur de connexion au serveur",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void modifierProfil() {

        String prenom = champPrenom.getText().toString().trim();
        String nom = champNom.getText().toString().trim();
        String telephone = champTelephone.getText().toString().trim();
        String photoUrl = champPhotoUrl.getText().toString().trim();
        String nouveauMotDePasse =
                champMotDePasse.getText().toString().trim();

        if (prenom.isEmpty()
                || nom.isEmpty()
                || telephone.isEmpty()
                || photoUrl.isEmpty()) {

            Toast.makeText(
                    this,
                    "Veuillez remplir tous les champs obligatoires.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        apiService.getUserById(userId).enqueue(new Callback<User>() {

            @Override
            public void onResponse(
                    Call<User> call,
                    Response<User> response) {

                if (!response.isSuccessful() || response.body() == null) {

                    Toast.makeText(
                            ProfilActivity.this,
                            "Impossible de récupérer le profil.",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                User user = response.body();

                user.setPrenom(prenom);
                user.setNom(nom);
                user.setTelephone(telephone);
                user.setPhotoUrl(photoUrl);

                if (!nouveauMotDePasse.isEmpty()) {
                    user.setPassword(nouveauMotDePasse);
                }

                apiService.updateUser(userId, user)
                        .enqueue(new Callback<User>() {

                            @Override
                            public void onResponse(
                                    Call<User> call,
                                    Response<User> response) {

                                if (response.isSuccessful()) {

                                    champMotDePasse.setText("");

                                    Toast.makeText(
                                            ProfilActivity.this,
                                            "Profil modifié avec succès !",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                } else {

                                    Toast.makeText(
                                            ProfilActivity.this,
                                            "Erreur lors de la modification.",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }

                            @Override
                            public void onFailure(
                                    Call<User> call,
                                    Throwable t) {

                                Toast.makeText(
                                        ProfilActivity.this,
                                        "Impossible de contacter le serveur.",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });
            }

            @Override
            public void onFailure(
                    Call<User> call,
                    Throwable t) {

                Toast.makeText(
                        ProfilActivity.this,
                        "Erreur de connexion au serveur.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void deconnecter() {

        SharedPreferences preferences =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        preferences.edit()
                .clear()
                .apply();

        Intent intent = new Intent(this, ConnexionActivity.class);

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }
}