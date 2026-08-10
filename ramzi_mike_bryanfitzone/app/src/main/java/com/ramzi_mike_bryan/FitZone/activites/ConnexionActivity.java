package com.ramzi_mike_bryan.FitZone.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ramzi_mike_bryan.FitZone.R;

import android.util.Log;
import android.widget.Toast;

import com.ramzi_mike_bryan.FitZone.api.RetrofitClient;
import com.ramzi_mike_bryan.FitZone.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnexionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        EditText champEmail = findViewById(R.id.editTextEmail);
        EditText champMotDePasse = findViewById(R.id.editPasswordConnexion);
        Button boutonConnexion = findViewById(R.id.boutonConnexion);
        TextView boutonInscription = findViewById(R.id.boutonInscription);

        boutonConnexion.setOnClickListener(v -> {

            String email = champEmail.getText().toString().trim();
            String password = champMotDePasse.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                        ConnexionActivity.this,
                        "Veuillez remplir tous les champs.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            RetrofitClient.getApiService()
                    .getUserByEmail(email)
                    .enqueue(new Callback<List<User>>() {

                        @Override
                        public void onResponse(
                                Call<List<User>> call,
                                Response<List<User>> response) {

                            if (!response.isSuccessful() || response.body() == null) {
                                Toast.makeText(
                                        ConnexionActivity.this,
                                        "Erreur lors de la connexion.",
                                        Toast.LENGTH_SHORT
                                ).show();
                                return;
                            }

                            List<User> users = response.body();

                            if (users.isEmpty()) {
                                Toast.makeText(
                                        ConnexionActivity.this,
                                        "Aucun compte trouvé avec cet email.",
                                        Toast.LENGTH_SHORT
                                ).show();
                                return;
                            }

                            User user = users.get(0);

                            if (user.getPassword().equals(password)) {

                                Toast.makeText(
                                        ConnexionActivity.this,
                                        "Connexion réussie !",
                                        Toast.LENGTH_SHORT
                                ).show();

                                Intent intent = new Intent(
                                        ConnexionActivity.this,
                                        AccueilActivity.class
                                );

                                startActivity(intent);
                                finish();

                            } else {

                                Toast.makeText(
                                        ConnexionActivity.this,
                                        "Mot de passe incorrect.",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }

                        @Override
                        public void onFailure(
                                Call<List<User>> call,
                                Throwable t) {

                            Toast.makeText(
                                    ConnexionActivity.this,
                                    "Impossible de contacter le serveur.",
                                    Toast.LENGTH_LONG
                            ).show();

                            Log.e("FITZONE_API", "Erreur", t);
                        }
                    });
        });

        RetrofitClient.getApiService().getUsers().enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<User> users = response.body();

                    Log.d("FITZONE_API", "Nombre utilisateurs : " + users.size());

                    for (User user : users) {
                        Log.d("FITZONE_API",
                                user.getUsername() + " - " + user.getEmail());
                    }

                } else {
                    Log.e("FITZONE_API", "Erreur HTTP : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("FITZONE_API", "Erreur connexion : " + t.getMessage());
            }
        });
    }
}