package com.ramzi_mike_bryan.FitZone.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

public class InscriptionActivity extends AppCompatActivity {

    private EditText champUsername;
    private EditText champPrenom;
    private EditText champNom;
    private EditText champEmail;
    private EditText champTelephone;
    private EditText champMotDePasse;
    private EditText champConfirmationMotDePasse;
    private TextView boutonConnexion;
    private Button boutonInscription;
    private EditText champPhotoUrl;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        // Récupération des champs du XML
        champUsername = findViewById(R.id.editUsernameInscription);
        champPrenom = findViewById(R.id.editPrenomInscription);
        champNom = findViewById(R.id.editNomInscription);
        champEmail = findViewById(R.id.editEmailInscription);
        champTelephone = findViewById(R.id.editTelephoneInscription);
        champMotDePasse = findViewById(R.id.editPasswordInscription);
        champConfirmationMotDePasse = findViewById(R.id.editConfirmationPasswordInscription);
        champPhotoUrl = findViewById(R.id.editPhotoUrlInscription);

        boutonInscription = findViewById(R.id.boutonCreerCompte);
        boutonConnexion = findViewById(R.id.boutonRetourConnexion);

        // Retrofit
        apiService = RetrofitClient.getRetrofitInstance()
                .create(ApiService.class);

        boutonInscription.setOnClickListener(v -> inscrireUtilisateur());

        boutonConnexion.setOnClickListener(v -> {

            Intent intent = new Intent(
                    InscriptionActivity.this,
                    ConnexionActivity.class
            );

            startActivity(intent);
            finish();
        });
    }

    private void inscrireUtilisateur() {

        String username = champUsername.getText().toString().trim();
        String prenom = champPrenom.getText().toString().trim();
        String nom = champNom.getText().toString().trim();
        String email = champEmail.getText().toString().trim();
        String telephone = champTelephone.getText().toString().trim();
        String motDePasse = champMotDePasse.getText().toString().trim();
        String confirmationMotDePasse = champConfirmationMotDePasse.getText().toString().trim();
        String photoUrl = champPhotoUrl.getText().toString().trim();

        // Vérification des champs
        if (username.isEmpty() ||
                prenom.isEmpty() ||
                nom.isEmpty() ||
                email.isEmpty() ||
                telephone.isEmpty() ||
                motDePasse.isEmpty()) {

            Toast.makeText(
                    this,
                    "Veuillez remplir tous les champs",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }
        if (!motDePasse.equals(confirmationMotDePasse)) {

            Toast.makeText(
                    this,
                    "Les mots de passe ne correspondent pas.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Création du nouvel utilisateur
        User nouvelUtilisateur = new User();

        nouvelUtilisateur.setUsername(username);
        nouvelUtilisateur.setPrenom(prenom);
        nouvelUtilisateur.setNom(nom);
        nouvelUtilisateur.setEmail(email);
        nouvelUtilisateur.setTelephone(telephone);
        nouvelUtilisateur.setPassword(motDePasse);

        // Valeurs par défaut
        nouvelUtilisateur.setPhotoUrl(photoUrl);        nouvelUtilisateur.setEnrolledProgramIds(new java.util.ArrayList<>());
        nouvelUtilisateur.setCompletedSeanceIds(new java.util.ArrayList<>());
        nouvelUtilisateur.setQuizResults(new java.util.ArrayList<>());

        // Envoi au JSON Server
        apiService.createUser(nouvelUtilisateur).enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful() && response.body() != null) {

                    Toast.makeText(
                            InscriptionActivity.this,
                            "Compte créé avec succès !",
                            Toast.LENGTH_SHORT
                    ).show();

                    // Retour vers la connexion
                    Intent intent = new Intent(
                            InscriptionActivity.this,
                            ConnexionActivity.class
                    );

                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(
                            InscriptionActivity.this,
                            "Impossible de créer le compte",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Toast.makeText(
                        InscriptionActivity.this,
                        "Impossible de contacter le serveur",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}