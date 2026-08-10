package com.ramzi_mike_bryan.FitZone.activites;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ramzi_mike_bryan.FitZone.R;
import com.api.ApiService;
import com.api.RetrofitClient;
import com.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilActivity extends AppCompatActivity {

    private TextView texteNom;
    private TextView texteUsername;
    private TextView texteEmail;
    private TextView texteTelephone;

    private Button boutonModifierProfil;
    private Button boutonDeconnexion;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Récupération des éléments du XML
        texteNom = findViewById(R.id.texteNom);
        texteUsername = findViewById(R.id.texteUsername);
        texteEmail = findViewById(R.id.texteEmail);
        texteTelephone = findViewById(R.id.texteTelephone);

        boutonModifierProfil = findViewById(R.id.boutonModifierProfil);
        boutonDeconnexion = findViewById(R.id.boutonDeconnexion);

        // Création du service Retrofit
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        chargerProfil();

        boutonDeconnexion.setOnClickListener(v -> {
            finish();
        });
    }

    private void chargerProfil() {

        String userId = getIntent().getStringExtra("USER_ID");

        if (userId == null) {
            Toast.makeText(
                    this,
                    "Utilisateur introuvable",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        apiService.getUserById(userId).enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful() && response.body() != null) {

                    User user = response.body();

                    texteNom.setText(
                            user.getPrenom() + " " + user.getNom()
                    );

                    texteUsername.setText(
                            "Nom d'utilisateur : " + user.getUsername()
                    );

                    texteEmail.setText(
                            "Courriel : " + user.getEmail()
                    );

                    texteTelephone.setText(
                            "Téléphone : " + user.getTelephone()
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
            public void onFailure(Call<User> call, Throwable t) {

                Toast.makeText(
                        ProfilActivity.this,
                        "Erreur de connexion au serveur",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}